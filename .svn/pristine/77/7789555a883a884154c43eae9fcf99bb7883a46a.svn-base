package virtualui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VirtualUIBasicElement implements VirtualUIListener {
	private int m_iId;
	private int m_iTop;
	private int m_iLeft;
	private int m_iWidth;
	private int m_iHeight;
	private String m_sPaddingValue;
	private boolean m_bVisible;
	private boolean m_bEnabled;
	private int m_iViewSeq;
	private String m_sBackgroundColor;
	private String m_sForegroundColor;
	private int m_iStroke;
	private String m_sStrokeColor;
	private boolean m_bGradient;
	private String m_sCornerRadius;
	private String m_sValue;
	private String m_sTextAlign;
	private int m_iTextSize;
	private String m_sTextStyle;
	private boolean m_bFocusWhenShow;
	private String m_sValueMirrorId;
	private String m_sStatus;
	private JSONArray m_oKeyCodeArray;
	private String[] m_sValueList;
	// Shadow
	private int m_iShadowTop;
	private int m_iShadowLeft;
	private int m_iShadowRadius;
	private String m_sShadowColor;
	
	private String m_sType;
	
	private VirtualUITerm m_oParentTerm;
	private VirtualUIForm m_oParentForm;
	private VirtualUIBasicElement m_oParentElement;
	private VirtualUIBasicElement m_oReferenceElement;
	
	// List of child basic elements
	private ArrayList<VirtualUIBasicElement> m_oChildElementList;
	
	// Flag to determine the click event
	private boolean m_bHaveClickEvent;
	
	// Flag to determine the long click event
	private boolean m_bHaveLongClickEvent;
	
	// Flag to determine the Swipe Top event
	private boolean m_bHaveSwipeTopEvent;
	
	// Flag to determine the Swipe Bottom event
	private boolean m_bHaveSwipeBottomEvent;
	
	// Flag to determine the Swipe Right event
	private boolean m_bHaveSwipeRightEvent;
	
	// Flag to determine the Swipe Left event
	private boolean m_bHaveSwipeLeftEvent;
	
	// Flag to determine the Value Changed event
	private boolean m_bHaveValueChangedEvent;
	
	// Flag to determine the Forward event
	private boolean m_bHaveForwardEvent;
	
	// Server Request Node for events
	private HashMap<String, VirtualUIServerRequest> m_oEventServerRequest;
	
	// Forward Request Node for events
	private HashMap<String, VirtualUIForwardRequest> m_oEventForwardRequest;
	
	// Action for events
	private HashMap<String, VirtualUIAction> m_oEventAction;
	
	// List of timer
	private HashMap<String, VirtualUITimer> m_oTimerList;
	
	private boolean m_bShow;
	
	private boolean m_bExist;
	
	// Constructor
	public VirtualUIBasicElement(){
		m_iId = 0;
		m_oParentTerm = null;
		m_oParentForm = null;
		m_oParentElement = null;
		m_oReferenceElement = null;
		m_oChildElementList = new ArrayList<VirtualUIBasicElement>();
		m_bVisible = true;
		m_bShow = false;
		m_sValue = "";
		m_bFocusWhenShow = false;
		m_bEnabled = true;
		m_iViewSeq = 0;
		m_bGradient = false;
		m_sCornerRadius = "";
		m_bHaveClickEvent = false;
		m_bHaveLongClickEvent = false;
		m_bHaveSwipeRightEvent = false;
		m_bHaveSwipeLeftEvent = false;
		m_bHaveValueChangedEvent = false;
		m_oEventServerRequest = new HashMap<String, VirtualUIServerRequest>();
		m_oEventForwardRequest = new HashMap<String, VirtualUIForwardRequest>();
		m_oEventAction = new HashMap<String, VirtualUIAction>();
		// Add Event Server Request
		m_oEventServerRequest.put("click", new VirtualUIServerRequest());
		m_oEventServerRequest.put("long_click", new VirtualUIServerRequest());
		m_oEventServerRequest.put("swipe_top", new VirtualUIServerRequest());
		m_oEventServerRequest.put("swipe_botton", new VirtualUIServerRequest());
		m_oEventServerRequest.put("swipe_left", new VirtualUIServerRequest());
		m_oEventServerRequest.put("swipe_right", new VirtualUIServerRequest());
		m_oEventServerRequest.put("value_changed", new VirtualUIServerRequest());
		m_oEventServerRequest.put("forward", new VirtualUIServerRequest());
		m_oEventServerRequest.put("keyboard", new VirtualUIServerRequest());
		// Add Forward Request
		m_oEventForwardRequest.put("forward", new VirtualUIForwardRequest());
		// Add Event Action Request
		m_oEventAction.put("click", new VirtualUIAction());
		m_oEventAction.put("long_click", new VirtualUIAction());
		m_oEventAction.put("swipe_top", new VirtualUIAction());
		m_oEventAction.put("swipe_botton", new VirtualUIAction());
		m_oEventAction.put("swipe_left", new VirtualUIAction());
		m_oEventAction.put("swipe_right", new VirtualUIAction());
		m_oEventAction.put("value_changed", new VirtualUIAction());
		m_oEventAction.put("forward", new VirtualUIAction());
		m_oEventAction.put("keyboard", new VirtualUIAction());
		m_oTimerList = new HashMap<String, VirtualUITimer>();
		m_iStroke = 0;
		m_sStrokeColor = "";
		m_sPaddingValue = "";
		m_oKeyCodeArray = new JSONArray();
		m_sValueList = new String[5];
		m_iShadowTop = 0;
		m_iShadowLeft = 0;
		m_iShadowRadius = 0;
		m_sShadowColor = "";
		
		m_bExist = false;
	}
	
	public void setParent(VirtualUIBasicElement oElement){
		m_oParentElement = oElement;
	}
	
	public VirtualUIBasicElement getParent(){
		return m_oParentElement;
	}
	
	public void setReferenceElement(VirtualUIBasicElement oElement){
		m_oReferenceElement = oElement;
	}
	
	public VirtualUIBasicElement getReferenceElement(){
		return m_oReferenceElement;
	}
	
	public ArrayList<VirtualUIBasicElement> getChilds() {
		return m_oChildElementList;
	}
	
	public void setParentTerm(VirtualUITerm oTerm){
		m_oParentTerm = oTerm;
		
		// Also set all children's parent term
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			oBasicElement.setParentTerm(oTerm);
		}
	}
	
	public VirtualUITerm getParentTerm(){
		return m_oParentTerm;
	}
	
	public void setParentForm(VirtualUIForm oForm){
		m_oParentForm = oForm;
		
		if(m_oParentForm != null && m_iId == 0){
			m_iId = m_oParentForm.getAvailableUIId();
		}
		
		if(m_oParentForm != null){
			if(m_iId == 0){
				m_iId = m_oParentForm.getAvailableUIId();
			}
			
			if(this.getUIType() == HeroActionProtocol.View.Type.FRAME || this.getUIType() == HeroActionProtocol.View.Type.SCROLL_FRAME)
				m_oParentForm.addListener(this);
		}
		
		// Also set all children's parent form
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			oBasicElement.setParentForm(oForm);
		}
	}
	
	public VirtualUIForm getParentForm(){
		return m_oParentForm;
	}
	
	public void setUIType(String type){
		m_sType = type;
	}
	
	public String getUIType(){
		return m_sType;
	}
	
	public void setValueMirrorId(String sValueMirrorId){
		m_sValueMirrorId = sValueMirrorId;
	}
	
	public String getValueMirrorId(){
		return m_sValueMirrorId;
	}
	
	public void show(){
		m_bShow = true;
		
		// Also show all children
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			oBasicElement.show();
		}
	}
	
	// Remove myself from client
	public void removeMyself() {
		JSONObject oView = new JSONObject();
		JSONObject oDo = new JSONObject();
		try {
			oDo.put(HeroActionProtocol.View.Do.Remove.KEY, "");
			
			oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
			oView.put(HeroActionProtocol.View.Do.KEY, oDo);
		}
		catch (Exception e) {}
		
		if (m_oParentTerm != null) {	//	For element attached to parent
			m_oParentTerm.appendPacket(oView);
			
			if(this.getUIType() == HeroActionProtocol.View.Type.FRAME || this.getUIType() == HeroActionProtocol.View.Type.SCROLL_FRAME){
				// Remove the frame from parent form
				m_oParentForm.removeChild(this.getId());
			}
			
			removeParentListener();
		}
		
		// Remove myself from parent
		if (m_oParentElement != null)
			this.getParent().removeChild(this.getId());
		
		// Remove all child elements from memory
		m_oChildElementList.clear();
	}
	
	// Remove the listener of Form
	public void removeParentListener(){
		if(m_oParentForm != null){
			for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
				oBasicElement.removeParentListener();
			}
			
			m_oParentForm.addListenerForRemove(this);
		}
	}
	
	// Trigger auto-click
	public void doAutoClick(int iDelay) {
		JSONObject oView = new JSONObject();
		JSONObject oDo = new JSONObject();
		JSONObject oTriggerEvent = new JSONObject();
		try {
			oTriggerEvent.put(HeroActionProtocol.View.Do.TriggerEvent.Type.KEY, HeroActionProtocol.View.Do.TriggerEvent.Type.CLICK);
			oTriggerEvent.put(HeroActionProtocol.View.Do.TriggerEvent.Delay.KEY, iDelay);
			oDo.put(HeroActionProtocol.View.Do.TriggerEvent.KEY, oTriggerEvent);
			oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
			oView.put(HeroActionProtocol.View.Do.KEY, oDo);
		}
		catch (Exception e) {}
		m_oParentTerm.appendPacket(oView);
	}
	
	public void bringToTop(){
		if (!m_bShow)
			return;
		
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oAttribute = new JSONObject();
		JSONObject oDo = new JSONObject();
		
		try {
			oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, true);
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, true);
			oDo.put(HeroActionProtocol.View.Do.Top.KEY, new JSONObject());
			
			oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
			oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			oView.put(HeroActionProtocol.View.Do.KEY, oDo);
		}
		catch (Exception e) {}
		
		m_oParentTerm.appendPacket(oView);
		
		// After bringToTop, move the specified frame to the end of child frame list
		// to make sure display order of frames is correct
		if(this instanceof VirtualUIFrame)
			m_oParentForm.moveChildToEnd(this.getId());
	}
	
	public void setReplaceValue(String sReplaceValueRegex, String sReplaceValue){
		if (!m_bShow)
			return;
		
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oReplaceValue = new JSONObject();
		JSONObject oDo = new JSONObject();
		
		try {
			
			oReplaceValue.put(HeroActionProtocol.View.Do.ReplaceValue.Regex.KEY, sReplaceValueRegex);
			oReplaceValue.put(HeroActionProtocol.View.Do.ReplaceValue.Value.KEY, sReplaceValue);
			
			oDo.put(HeroActionProtocol.View.Do.ReplaceValue.KEY, oReplaceValue);
			
			oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
			
			oView.put(HeroActionProtocol.View.Do.KEY, oDo);
		}
		catch (Exception e) {}
		
		m_oParentTerm.appendPacket(oView);
	}
	
	public void attachChild(VirtualUIBasicElement oBasicElement){
		// skip attach if object not exist
		if (oBasicElement.getExist() == false)
			return;
		
		if(m_oParentForm != null && m_iId == 0){
			m_iId = m_oParentForm.getAvailableUIId();
		}
		
		m_oChildElementList.add(oBasicElement);
		
		oBasicElement.setParent(this);
		oBasicElement.setParentForm(m_oParentForm);
		oBasicElement.setParentTerm(m_oParentTerm);
		
		if (m_bShow){
			// Show the child
			oBasicElement.show();
		}
	}
	
	public void removeChild(int iId){
		
		// Loop all children
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			if(oBasicElement.getId() == iId){
				
				if(oBasicElement.getUIType() == HeroActionProtocol.View.Type.FRAME || oBasicElement.getUIType() == HeroActionProtocol.View.Type.SCROLL_FRAME){
					// Remove Current Form from the listener list of Term
					if (m_oParentForm != null)
						m_oParentForm.removeChild(oBasicElement.getId());
				}
				
				if (m_bShow){
					// Create packet to remove UI
					JSONObject oView = new JSONObject();
					JSONObject oDo = new JSONObject();
					
					try {
						oDo.put(HeroActionProtocol.View.Do.Remove.KEY, new JSONObject());
						
						oView.put(HeroActionProtocol.View.Id.KEY, oBasicElement.getIDForPosting());
						oView.put(HeroActionProtocol.View.Do.KEY, oDo);
					}
					catch (Exception e) {}
					
					m_oParentTerm.appendPacket(oView);
				}
				
				oBasicElement.removeParentListener();
				m_oChildElementList.remove(oBasicElement);
				
				break;
			}
		}
		
	}
	
	public void removeAllChildren(){
		
		if (m_bShow){
			// Create packet to remove UI
			JSONObject oView = new JSONObject();
			JSONObject oDo = new JSONObject();
			
			try {
				oDo.put(HeroActionProtocol.View.Do.RemoveChildren.KEY, new JSONObject());
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Do.KEY, oDo);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
		
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			oBasicElement.removeParentListener();
		}
		m_oChildElementList.clear();
	}
	
	public void setFocus(){
		if(m_oParentTerm != null){
			// Create packet to remove UI
			JSONObject oView = new JSONObject();
			JSONObject oDo = new JSONObject();
			
			try {
				oDo.put(HeroActionProtocol.View.Do.Focus.KEY, new JSONObject());
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Do.KEY, oDo);
			}
			catch (Exception e) {}
		
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getChildCount(){
		return m_oChildElementList.size();
	}
	
	public boolean isChildId(int iId){
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			if(oBasicElement.getId() == iId){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isShow(){
		return m_bShow;
	}
	
	public void finishShow(){
		m_bShow = false;
	}
	
	public int getId(){
		return m_iId;
	}
	
	public void setId(int iId){
		m_iId = iId;
	}
	
	public int getTop(){
		return m_iTop;
	}
	
	public void setTop(int iTop){
		Boolean bNeedUpdateClient = false;
		
		if(m_iTop != iTop){
			m_iTop = iTop;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Top.KEY, m_iTop);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getLeft(){
		return m_iLeft;
	}
	
	public void setLeft(int iLeft){
		Boolean bNeedUpdateClient = false;
		
		if(m_iLeft != iLeft){
			m_iLeft = iLeft;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Left.KEY, m_iLeft);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getWidth(){
		return m_iWidth;
	}
	
	public void setWidth(int iWidth){
		Boolean bNeedUpdateClient = false;
		
		if(m_iWidth != iWidth){
			m_iWidth = iWidth;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Width.KEY, m_iWidth);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getHeight(){
		return m_iHeight;
	}
	
	public void setHeight(int iHeight){
		Boolean bNeedUpdateClient = false;
		
		if(m_iHeight != iHeight){
			m_iHeight = iHeight;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Height.KEY, m_iHeight);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public String getPaddingValue(){
		return m_sPaddingValue;
	}
	
	public void setPaddingValue(String sPaddingValue){
		if (sPaddingValue.isEmpty())
			return;
		
		Boolean bNeedUpdateClient = false;
		
		if(!m_sPaddingValue.equals(sPaddingValue)) {
			m_sPaddingValue = sPaddingValue;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
//				oAttribute.put(HeroActionProtocol.View.Attribute.Top.KEY, m_iTop + m_iPaddingValue);
//				oAttribute.put(HeroActionProtocol.View.Attribute.Left.KEY, m_iLeft + m_iPaddingValue);
//				oAttribute.put(HeroActionProtocol.View.Attribute.Width.KEY, m_iWidth - (m_iPaddingValue * 2));
//				oAttribute.put(HeroActionProtocol.View.Attribute.Height.KEY, m_iHeight - (m_iPaddingValue * 2));
				oAttribute.put(HeroActionProtocol.View.Attribute.Padding.KEY, m_sPaddingValue);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public boolean getVisible(){
		return m_bVisible;
	}
	
	public void setVisible(boolean bVisible){
		m_bVisible = bVisible;
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, m_bVisible);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public String getBackgroundColor(){
		return m_sBackgroundColor;
	}
	
	public void setBackgroundColor(String sBackgroundColor){
		m_sBackgroundColor = sBackgroundColor;
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Background.KEY, m_sBackgroundColor);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public String getForegroundColor(){
		return m_sForegroundColor;
	}
	
	public void setForegroundColor(String sForegroundColor){
		m_sForegroundColor = sForegroundColor;
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Foreground.KEY, m_sForegroundColor);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getStroke(){
		return m_iStroke;
	}
	
	public void setStroke(int iStroke){
		Boolean bNeedUpdateClient = false;
		
		if(m_iStroke != iStroke){
			m_iStroke = iStroke;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Stroke.KEY, m_iStroke);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public String getStrokeColor(){
		return m_sStrokeColor;
	}
	
	public void setStrokeColor(String sStrokeColor){
		m_sStrokeColor = sStrokeColor;
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.StrokeColor.KEY, m_sStrokeColor);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	// Click event
	public void setClickServerRequestNote(String sNote){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("click");
		oServerRequest.setServerRequestNote(sNote);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Event.KEY, this.getEventForPost());
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
		
	public String getClickServerRequestNote(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("click");
		return oServerRequest.getServerRequestNote();
	}
	
	public void addClickServerRequestSubmitElement(VirtualUIBasicElement oElement){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("click");
		oServerRequest.addServerRequestSubmitElement(oElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				if(this.getClickServerRequestSubmitId().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.SubmitId.KEY, this.getClickServerRequestSubmitId());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.ServerRequest.KEY, oJSONServerRequest);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray getClickServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("click");
		return oServerRequest.getServerRequestSubmitId();
	}
	
	public void clearClickServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("click");
		oServerRequest.clearServerRequestSubmitId();
	}
	
	public void setClickServerRequestBlockUI(boolean bBlockUI){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("click");
		oServerRequest.setServerRequestBlockUI(bBlockUI);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				
				if(getClickServerRequestBlockUI())
					oJSONServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.BlockUI.KEY, true);
				else
					oJSONServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.BlockUI.KEY, false);
				oJSONServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.Timeout.KEY, getClickServerRequestTimeout());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.ServerRequest.KEY, oJSONServerRequest);
				
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public boolean getClickServerRequestBlockUI(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("click");
		return oServerRequest.getServerRequestBlockUI();
	}
	
	public void setClickServerRequestTimeout(int iTimeout){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("click");
		oServerRequest.setServerRequestTimeout(iTimeout);
	}
	
	public int getClickServerRequestTimeout(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("click");
		return oServerRequest.getServerRequestTimeout();
	}
	
	public void setClickHideKeyboard(boolean bAction){
		VirtualUIAction oAction = m_oEventAction.get("click");
		oAction.setHideKeyboard(bAction);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				
				JSONObject oJSONAction = new JSONObject();
				JSONObject oSystem = new JSONObject();
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				oSystem.put(HeroActionProtocol.System.HideKeyboard.KEY, "");
				oJSONAction.put(HeroActionProtocol.System.KEY, oSystem);
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Action.KEY, oJSONAction);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public boolean getClickHideKeyboard(){
		VirtualUIAction oAction = m_oEventAction.get("click");
		return oAction.getHideKeyboard();
	}
	
	public void setFocusWhenShow(boolean bFocus){
		m_bFocusWhenShow = bFocus;
	}
	
	public boolean getFocusWhenShow(){
		return m_bFocusWhenShow;
	}
	
	public void setClickReplaceValue(VirtualUIBasicElement oElement, String sReplaceValueRegex, String sReplaceValue){
		VirtualUIAction oAction = m_oEventAction.get("click");
		oAction.setReplaceValue(oElement, sReplaceValueRegex, sReplaceValue);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONAction = new JSONObject();
			
			try {
				if (oElement == null)
					oView.put(HeroActionProtocol.View.Id.KEY, HeroActionProtocol.View.Id.FOCUS);
				else
					oView.put(HeroActionProtocol.View.Id.KEY, oElement.getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				oJSONAction.put(HeroActionProtocol.View.KEY, oAction.buildActionViewArray());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Action.KEY, oJSONAction);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray buildClickActionViewArray(){
		VirtualUIAction oAction = m_oEventAction.get("click");
		return oAction.buildActionViewArray();
	}
	
	public int getClickReplaceValueCount(){
		VirtualUIAction oAction = m_oEventAction.get("click");
		return oAction.getReplaceValueCount();
	}
	
	public void setClickElementEnable(boolean bEnable){
		VirtualUIAction oAction = m_oEventAction.get("click");
		oAction.setClickElementEnable(getIDForPosting(), bEnable);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONAction = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				oJSONAction.put(HeroActionProtocol.View.KEY, oAction.buildActionViewArray());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Action.KEY, oJSONAction);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
						
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	// Set Click Action
	public void addClickVisibleElements(VirtualUIBasicElement oTargetElement, boolean bVisible){
		
		VirtualUIBasicElement oActionElement = new VirtualUIBasicElement();
		oActionElement.setReferenceElement(oTargetElement);
		oActionElement.setVisible(bVisible);
		
		VirtualUIAction oAction = m_oEventAction.get("click");
		oAction.addVisibleElement(oActionElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONAction = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, this.getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				oJSONAction.put(HeroActionProtocol.View.KEY, oAction.buildActionViewArray());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Action.KEY, oJSONAction);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	// Long Click
	public void setLongClickServerRequestNote(String sNote){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("long_click");
		oServerRequest.setServerRequestNote(sNote);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Event.KEY, this.getEventForPost());
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public String getLongClickServerRequestNote(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("long_click");
		return oServerRequest.getServerRequestNote();
	}
	
	public void addLongClickServerRequestSubmitElement(VirtualUIBasicElement oElement){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("long_click");
		oServerRequest.addServerRequestSubmitElement(oElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.LongClick.Id.KEY, "1");
				if(this.getLongClickServerRequestSubmitId().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.SubmitId.KEY, this.getLongClickServerRequestSubmitId());
				oClickEvent.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.KEY, oJSONServerRequest);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray getLongClickServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("long_click");
		return oServerRequest.getServerRequestSubmitId();
	}
	
	public void clearLongClickServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("long_click");
		oServerRequest.clearServerRequestSubmitId();
	}
	
	public void setLongClickServerRequestBlockUI(boolean bBlockUI){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("long_click");
		oServerRequest.setServerRequestBlockUI(bBlockUI);
	}
	
	public boolean getLongClickServerRequestBlockUI(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("long_click");
		return oServerRequest.getServerRequestBlockUI();
	}
	
	public void setLongClickReplaceValue(VirtualUIBasicElement oElement, String sReplaceValueRegex, String sReplaceValue){
		VirtualUIAction oAction = m_oEventAction.get("long_click");
		oAction.setReplaceValue(oElement, sReplaceValueRegex, sReplaceValue);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONAction = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				oJSONAction.put(HeroActionProtocol.View.KEY, oAction.buildActionViewArray());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Action.KEY, oJSONAction);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray buildLongClickActionViewArray(){
		VirtualUIAction oAction = m_oEventAction.get("long_click");
		return oAction.buildActionViewArray();
	}
	
	public int getLongClickReplaceValueCount(){
		VirtualUIAction oAction = m_oEventAction.get("long_click");
		return oAction.getReplaceValueCount();
	}
	
	public void setLongClickElementEnable(boolean bEnable){
		VirtualUIAction oAction = m_oEventAction.get("long_click");
		oAction.setClickElementEnable(getIDForPosting(), bEnable);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONAction = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				oJSONAction.put(HeroActionProtocol.View.KEY, oAction.buildActionViewArray());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Action.KEY, oJSONAction);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
						
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	// Swipe Top
	public void setSwipeTopServerRequestNote(String sNote){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_top");
		oServerRequest.setServerRequestNote(sNote);
	}
	
	public String getSwipeTopServerRequestNote(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_top");
		return oServerRequest.getServerRequestNote();
	}
	
	public void addSwipeTopServerRequestSubmitElement(VirtualUIBasicElement oElement){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_top");
		oServerRequest.addServerRequestSubmitElement(oElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.SwipeTop.Id.KEY, "1");
				if(this.getSwipeTopServerRequestSubmitId().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.SubmitId.KEY, this.getSwipeTopServerRequestSubmitId());
				oClickEvent.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.KEY, oJSONServerRequest);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray getSwipeTopServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_top");
		return oServerRequest.getServerRequestSubmitId();
	}
	
	public void clearSwipeTopServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_top");
		oServerRequest.clearServerRequestSubmitId();
	}
	
	public void setSwipeTopServerRequestBlockUI(boolean bBlockUI){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_top");
		oServerRequest.setServerRequestBlockUI(bBlockUI);
	}
	
	public boolean getSwipeTopServerRequestBlockUI(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_top");
		return oServerRequest.getServerRequestBlockUI();
	}
	
	// Swipe Bottom
	public void setSwipeBottomServerRequestNote(String sNote){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_bottom");
		oServerRequest.setServerRequestNote(sNote);
	}
	
	public String getSwipeBottomServerRequestNote(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_bottom");
		return oServerRequest.getServerRequestNote();
	}
	
	public void addSwipeBottomServerRequestSubmitElement(VirtualUIBasicElement oElement){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_bottom");
		oServerRequest.addServerRequestSubmitElement(oElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.SwipeBottom.Id.KEY, "1");
				if(this.getSwipeBottomServerRequestSubmitId().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.SubmitId.KEY, this.getSwipeBottomServerRequestSubmitId());
				oClickEvent.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.KEY, oJSONServerRequest);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray getSwipeBottomServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_bottom");
		return oServerRequest.getServerRequestSubmitId();
	}
	
	public void clearSwipeBottomServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_bottom");
		oServerRequest.clearServerRequestSubmitId();
	}
	
	public void setSwipeBottomServerRequestBlockUI(boolean bBlockUI){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_bottom");
		oServerRequest.setServerRequestBlockUI(bBlockUI);
	}
	
	public boolean getSwipeBottomServerRequestBlockUI(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_bottom");
		return oServerRequest.getServerRequestBlockUI();
	}
	
	// Swipe Left
	public void setSwipeLeftServerRequestNote(String sNote){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_left");
		oServerRequest.setServerRequestNote(sNote);
	}
	
	public String getSwipeLeftServerRequestNote(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_left");
		return oServerRequest.getServerRequestNote();
	}
	
	public void addSwipeLeftServerRequestSubmitElement(VirtualUIBasicElement oElement){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_left");
		oServerRequest.addServerRequestSubmitElement(oElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.SwipeLeft.Id.KEY, "1");
				if(this.getSwipeLeftServerRequestSubmitId().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.SubmitId.KEY, this.getSwipeLeftServerRequestSubmitId());
				oClickEvent.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.KEY, oJSONServerRequest);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray getSwipeLeftServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_left");
		return oServerRequest.getServerRequestSubmitId();
	}
	
	public void clearSwipeLeftServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_left");
		oServerRequest.clearServerRequestSubmitId();
	}
	
	public void setSwipeLeftServerRequestBlockUI(boolean bBlockUI){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_left");
		oServerRequest.setServerRequestBlockUI(bBlockUI);
	}
	
	public boolean getSwipeLeftServerRequestBlockUI(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_left");
		return oServerRequest.getServerRequestBlockUI();
	}
	
	// Swipe Right
	public void setSwipeRightServerRequestNote(String sNote){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_right");
		oServerRequest.setServerRequestNote(sNote);
	}
		
	public String getSwipeRightServerRequestNote(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_right");
		return oServerRequest.getServerRequestNote();
	}
	
	public void addSwipeRightServerRequestSubmitElement(VirtualUIBasicElement oElement){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_right");
		oServerRequest.addServerRequestSubmitElement(oElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oClickEvent = new JSONObject();
			JSONArray oClickEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oClickEvent.put(HeroActionProtocol.View.Event.SwipeRight.Id.KEY, "1");
				if(this.getSwipeRightServerRequestSubmitId().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.SubmitId.KEY, this.getSwipeRightServerRequestSubmitId());
				oClickEvent.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.KEY, oJSONServerRequest);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray getSwipeRightServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_right");
		return oServerRequest.getServerRequestSubmitId();
	}
	
	public void clearSwipeRightServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_right");
		oServerRequest.clearServerRequestSubmitId();
	}
	
	public void setSwipeRightServerRequestBlockUI(boolean bBlockUI){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_right");
		oServerRequest.setServerRequestBlockUI(bBlockUI);
	}
	
	public boolean getSwipeRightServerRequestBlockUI(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("swipe_right");
		return oServerRequest.getServerRequestBlockUI();
	}
	
	// Value Changed
	public void setValueChangedServerRequestNote(String sNote){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
		oServerRequest.setServerRequestNote(sNote);
	}
		
	public String getValueChangedServerRequestNote(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
		return oServerRequest.getServerRequestNote();
	}
	
	public void addValueChangedServerRequestSubmitElement(VirtualUIBasicElement oElement){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
		oServerRequest.addServerRequestSubmitElement(oElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oValueChangedEvent = new JSONObject();
			JSONArray oValueChangedEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oValueChangedEvent.put(HeroActionProtocol.View.Event.ValueChanged.Id.KEY, "1");
				if(this.getValueChangedServerRequestSubmitId().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.ValueChanged.ServerRequest.SubmitId.KEY, this.getValueChangedServerRequestSubmitId());
				oValueChangedEvent.put(HeroActionProtocol.View.Event.ValueChanged.ServerRequest.KEY, oJSONServerRequest);
				oValueChangedEventArray.put(oValueChangedEvent);
				oEvent.put(HeroActionProtocol.View.Event.ValueChanged.KEY, oValueChangedEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray getValueChangedServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
		return oServerRequest.getServerRequestSubmitId();
	}
	
	public void clearValueChangedServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
		oServerRequest.clearServerRequestSubmitId();
	}
	
	public void setValueChangedServerRequestBlockUI(boolean bBlockUI){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
		oServerRequest.setServerRequestBlockUI(bBlockUI);
	}
	
	public boolean getValueChangedServerRequestBlockUI(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
		return oServerRequest.getServerRequestBlockUI();
	}
	
	public void setValueChangedServerRequestTimeout(int iTimeout){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
		oServerRequest.setServerRequestTimeout(iTimeout);
	}
	
	public int getValueChangedServerRequestTimeout() {
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
		return oServerRequest.getServerRequestTimeout();
	}
	
	// Forward
	public void setForwardServerRequestNote(String sNote){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
		oServerRequest.setServerRequestNote(sNote);
	}
	
	public String getForwardServerRequestNote(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
		return oServerRequest.getServerRequestNote();
	}
	
	public void addForwardServerRequestSubmitElement(VirtualUIBasicElement oElement){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
		oServerRequest.addServerRequestSubmitElement(oElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oForwardEvent = new JSONObject();
			JSONArray oForwardEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.Id.KEY, "1");
				if(this.getForwardServerRequestSubmitId().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.SubmitId.KEY, this.getForwardServerRequestSubmitId());
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ServerRequest.KEY, oJSONServerRequest);
				oForwardEventArray.put(oForwardEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oForwardEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray getForwardServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
		return oServerRequest.getServerRequestSubmitId();
	}
	
	public void clearForwardServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
		oServerRequest.clearServerRequestSubmitId();
	}
	
	public void setForwardServerRequestBlockUI(boolean bBlockUI){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
		oServerRequest.setServerRequestBlockUI(bBlockUI);
	}
	
	public boolean getForwardServerRequestBlockUI(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
		return oServerRequest.getServerRequestBlockUI();
	}
	
	public void setForwardForwardRequestType(String sType){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		oForwardRequest.setForwardRequestType(sType);
	}
	
	public String getForwardForwardRequestType(){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		return oForwardRequest.getForwardRequestType();
	}
	
	public void setForwardForwardRequestAddress(String sAddress){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		oForwardRequest.setForwardRequestAddress(sAddress);
	}
	
	public String getForwardForwardRequestAddress(){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		return oForwardRequest.getForwardRequestAddress();
	}
	
	public void setForwardForwardRequestPort(int iPort){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		oForwardRequest.setForwardRequestPort(iPort);
	}
		
	public int getForwardForwardRequestPort(){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		return oForwardRequest.getForwardRequestPort();
	}
	
	public void setForwardForwardRequestValue(String sValue){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		oForwardRequest.setForwardRequestValue(sValue);
		
		if(m_bShow){
			// Increment event version
			oForwardRequest.incrementForwardRequestVersion();
			VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
			oServerRequest.setServerRequestNote(oForwardRequest.getForwardRequestVersion()+"");
			
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oForwardEvent = new JSONObject();
			JSONArray oForwardEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oForwardRequestJSON = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.Id.KEY, "Forward1");
				oForwardRequestJSON.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Value.KEY, this.getForwardForwardRequestValue());
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.KEY, oForwardRequestJSON);
				
				oJSONServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.Note.KEY, this.getForwardServerRequestNote());
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ServerRequest.KEY, oJSONServerRequest);
								
				oForwardEventArray.put(oForwardEvent);
				oEvent.put(HeroActionProtocol.View.Event.Forward.KEY, oForwardEventArray);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public String getForwardForwardRequestValue(){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		// Clear value after get the value for posting
		String sValue = oForwardRequest.getForwardRequestValue();
		oForwardRequest.setForwardRequestValue("");
		return sValue;
	}
	
	public void setForwardForwardRequestTimeout(int iTimeout){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		oForwardRequest.setForwardRequestTimeout(iTimeout);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oForwardEvent = new JSONObject();
			JSONArray oForwardEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oForwardRequestJSON = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.Id.KEY, "Forward1");
				oForwardRequestJSON.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Timeout.KEY, iTimeout);
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.KEY, oForwardRequestJSON);
				
				oJSONServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.Note.KEY, this.getForwardServerRequestNote());
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ServerRequest.KEY, oJSONServerRequest);
				
				oForwardEventArray.put(oForwardEvent);
				oEvent.put(HeroActionProtocol.View.Event.Forward.KEY, oForwardEventArray);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getForwardForwardRequestTimeout(){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		return oForwardRequest.getForwardRequestTimeout();
	}
	
	public void setForwardForwardRequestDelay(int iDelay){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		oForwardRequest.setForwardRequestDelay(iDelay);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oForwardEvent = new JSONObject();
			JSONArray oForwardEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oForwardRequestJSON = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.Id.KEY, "Forward1");
				oForwardRequestJSON.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Delay.KEY, iDelay);
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.KEY, oForwardRequestJSON);
				
				oJSONServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.Note.KEY, this.getForwardServerRequestNote());
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ServerRequest.KEY, oJSONServerRequest);
				
				oForwardEventArray.put(oForwardEvent);
				oEvent.put(HeroActionProtocol.View.Event.Forward.KEY, oForwardEventArray);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
		
	public int getForwardForwardRequestDelay(){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		return oForwardRequest.getForwardRequestDelay();
	}
	
	public void setForwardForwardRequestBlockUI(boolean bBlockUI){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		oForwardRequest.setForwardRequestBlockUI(bBlockUI);
	}
	
	public boolean getForwardForwardRequestBlockUI(){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		return oForwardRequest.getForwardRequestBlockUI();
	}
	
	public long getForwardForwardRequestVersion(){
		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
		return oForwardRequest.getForwardRequestVersion();
	}
	
	// Keyboard event
	public void setKeyboardServerRequestNote(String sNote){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("keyboard");
		oServerRequest.setServerRequestNote(sNote);
	}
		
	public String getKeyboardServerRequestNote(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("keyboard");
		return oServerRequest.getServerRequestNote();
	}
	
	public void addKeyboardServerRequestSubmitElement(VirtualUIBasicElement oElement){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("keyboard");
		oServerRequest.addServerRequestSubmitElement(oElement);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oKeyboardEvent = new JSONObject();
			JSONArray oKeyboardEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONServerRequest = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.Id.KEY, "1");
				if(this.getKeyboardServerRequestSubmitId().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.Keyboard.ServerRequest.SubmitId.KEY, this.getKeyboardServerRequestSubmitId());
				if(this.getKeyboardServerRequestNote().length() > 0)
					oJSONServerRequest.put(HeroActionProtocol.View.Event.Keyboard.ServerRequest.Note.KEY, this.getKeyboardServerRequestNote());
				oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.ServerRequest.KEY, oJSONServerRequest);
				oKeyboardEventArray.put(oKeyboardEvent);
				oEvent.put(HeroActionProtocol.View.Event.Keyboard.KEY, oKeyboardEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public void setKeyboardReplaceValue(VirtualUIBasicElement oElement, String sReplaceValueRegex, String sReplaceValue){
		VirtualUIAction oAction = m_oEventAction.get("keyboard");
		oAction.setReplaceValue(oElement, sReplaceValueRegex, sReplaceValue);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oKeyboardEvent = new JSONObject();
			JSONArray oKeyboardEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			JSONObject oJSONAction = new JSONObject();
			
			try {
				if (oElement == null)
					oView.put(HeroActionProtocol.View.Id.KEY, HeroActionProtocol.View.Id.FOCUS);
				else
					oView.put(HeroActionProtocol.View.Id.KEY, oElement.getIDForPosting());
				oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.Id.KEY, "1");
				oJSONAction.put(HeroActionProtocol.View.KEY, oAction.buildActionViewArray());
				oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.Action.KEY, oJSONAction);
				oKeyboardEventArray.put(oKeyboardEvent);
				oEvent.put(HeroActionProtocol.View.Event.Keyboard.KEY, oKeyboardEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray buildKeyboardActionViewArray(){
		VirtualUIAction oAction = m_oEventAction.get("keyboard");
		return oAction.buildActionViewArray();
	}
	
	public int getKeyboardReplaceValueCount(){
		VirtualUIAction oAction = m_oEventAction.get("keyboard");
		return oAction.getReplaceValueCount();
	}
	
	public JSONArray getKeyboardServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("keyboard");
		return oServerRequest.getServerRequestSubmitId();
	}
	
	public void clearKeyboardServerRequestSubmitId(){
		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("keyboard");
		oServerRequest.clearServerRequestSubmitId();
	}
	
	public void addKeyboardKeyCode(int iKeyCode){
		m_oKeyCodeArray.put(iKeyCode);
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oKeyboardEvent = new JSONObject();
			JSONArray oKeyboardEventArray = new JSONArray();
			JSONObject oEvent = new JSONObject();
			
			try {
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.Id.KEY, "1");
				oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.KeyCode.KEY, m_oKeyCodeArray.toString());
				oKeyboardEventArray.put(oKeyboardEvent);
				oEvent.put(HeroActionProtocol.View.Event.Keyboard.KEY, oKeyboardEventArray);
				
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public JSONArray getKeyboardKeyCode(){
		return m_oKeyCodeArray;
	}
	
	public void clearKeyboardKeyCode(){
		m_oKeyCodeArray = new JSONArray();
	}
	
	public String getValue(){
		if (m_oParentTerm != null) {
			if (m_oParentTerm.getCurrentLanguageIndex() < m_sValueList.length) {
				if (m_sValueList[m_oParentTerm.getCurrentLanguageIndex()] != null) {
					return m_sValueList[m_oParentTerm.getCurrentLanguageIndex()];
				}
			}
		}
		
		return m_sValue;
	}
	
	// Get value by index
	public String getValueByIndex(int iIndex) {
		if (m_oParentTerm != null && iIndex < m_sValueList.length && m_sValueList[iIndex] != null)
			return m_sValueList[iIndex];
		return m_sValue;
	}
	
	// Change language index
	public void changeLanguageIndex(int iIndex) {
		setValueByIndex(iIndex);
		
		for (VirtualUIBasicElement oChildElement:getChilds()) {
			oChildElement.changeLanguageIndex(iIndex);
		}
	}
	
	// Assign element default value in different language index
	public void assignValueByIndex(int iIndex, String sValue) {
		if (iIndex >= m_sValueList.length) {
			// Index > defined array size
			// Resize the array
			m_sValueList = Arrays.copyOf(m_sValueList, iIndex+1);
		}
		
		m_sValueList[iIndex] = sValue;
		
		if (m_bShow) {
			if (m_oParentTerm != null) {
				if (iIndex == m_oParentTerm.getCurrentLanguageIndex()) {
					// The UI is shown ==> Modify UI
					// Create packet to update UI
					JSONObject oView = new JSONObject();
					JSONObject oAttribute = new JSONObject();
					
					try {
						oAttribute.put(HeroActionProtocol.View.Attribute.Value.KEY, getValue());
						
						oView.put(HeroActionProtocol.View.Id.KEY, this.getIDForPosting());
						oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
					}
					catch (Exception e) {}
					
					m_oParentTerm.appendPacket(oView);
				}
			}
		}
	}
	
	// Change element value according to index
	public void setValueByIndex(int iIndex) {
		if (iIndex >= m_sValueList.length) {
			// Value is not set before
		} else {
			String sValue = m_sValueList[iIndex];
			if (sValue != null) {
				setValue(sValue);
			}
		}
	}
	
	// Set value for LangResource
	public void setValue(String[] oValueList) {
		for (int i=0; i<oValueList.length; i++) {
			assignValueByIndex(i, oValueList[i]);
		}
		
		if (m_oParentTerm != null) {
			if (m_oParentTerm.getCurrentLanguageIndex() < m_sValueList.length) {
				m_sValue = m_sValueList[m_oParentTerm.getCurrentLanguageIndex()];
			}
		} else
			m_sValue = oValueList[0];
	}
	
	public void setValue(String sValue){
		m_sValue = sValue;
		
		if (m_oParentTerm != null) {
			for (int i=0; i<m_sValueList.length; i++) {
				if (i == m_oParentTerm.getCurrentLanguageIndex() || m_sValueList[i] == null) {
					// Must update the current index value and those index with null value
					m_sValueList[i] = sValue;
				}
			}
		}
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Value.KEY, getValue());
				
				oView.put(HeroActionProtocol.View.Id.KEY, this.getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
		
		return;
	}
	
	public String getStatus(){
		return m_sStatus;
	}
	
	public void setValue(int iId, String sValue){
		if(m_iId == iId){
			setValue(sValue);
			return;
		}
		
		// Search children
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			oBasicElement.setValue(iId, sValue);
		}
	}
	
	public void setStatus(int iId, String sStatus){
		if(m_iId == iId){
			m_sStatus = sStatus;
			return;
		}
		
		// Search children
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			oBasicElement.setStatus(iId, sStatus);
		}
	}
	
	public String getTextAlign(){
		return m_sTextAlign;
	}
	
	public void setTextAlign(String sTextAlign){
		m_sTextAlign = sTextAlign;
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.TextAlign.KEY, m_sTextAlign);
				
				oView.put(HeroActionProtocol.View.Id.KEY, this.getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public String getTextStyle() {
		return m_sTextStyle;
	}
	
	public void setTextStyle(String sTextStyle) {
		m_sTextStyle = sTextStyle;
		
		if(m_bShow) {
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.TextStyle.KEY, m_sTextStyle);
				
				oView.put(HeroActionProtocol.View.Id.KEY, this.getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getTextSize(){
		return m_iTextSize;
	}
	
	public void setTextSize(int iTextSize){
		m_iTextSize = iTextSize;
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.TextSize.KEY, m_iTextSize);
				
				oView.put(HeroActionProtocol.View.Id.KEY, this.getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getShadowTop(){
		return m_iShadowTop;
	}
	
	public void setShadowTop(int iShadowTop){
		Boolean bNeedUpdateClient = false;
		
		if(m_iShadowTop != iShadowTop){
			m_iShadowTop = iShadowTop;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			JSONObject oShadow = new JSONObject();
			
			try {
				oShadow.put(HeroActionProtocol.View.Attribute.Shadow.Top.KEY, m_iShadowTop);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
				oAttribute.put(HeroActionProtocol.View.Attribute.KEY, oShadow);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getShadowLeft(){
		return m_iShadowLeft;
	}
	
	public void setShadowLeft(int iShadowLeft){
		Boolean bNeedUpdateClient = false;
		
		if(m_iShadowLeft != iShadowLeft){
			m_iShadowLeft = iShadowLeft;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Shadow.Left.KEY, m_iShadowLeft);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getShadowRadius(){
		return m_iShadowRadius;
	}
	
	public void setShadowRadius(int iShadowRadius){
		Boolean bNeedUpdateClient = false;
		
		if(m_iShadowRadius != iShadowRadius){
			m_iShadowRadius = iShadowRadius;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Shadow.Radius.KEY, m_iShadowRadius);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public String getShadowColor(){
		return m_sShadowColor;
	}
	
	public void setShadowColor(String sShadowColor){
		m_sShadowColor = sShadowColor;
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Shadow.Color.KEY, m_sShadowColor);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public boolean getEnabled() {
		return m_bEnabled;
	}
	
	public void setEnabled(boolean bEnabled) {
		if(m_bEnabled != bEnabled){
			m_bEnabled = bEnabled;
		}
		
		if(m_bShow){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, m_bEnabled);
				
				oView.put(HeroActionProtocol.View.Id.KEY, this.getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public int getViewSeq(){
		return m_iViewSeq;
	}
	
	public void setViewSeq(int iViewSeq){
		Boolean bNeedUpdateClient = false;
		
		if(m_iViewSeq != iViewSeq){
			m_iViewSeq = iViewSeq;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.ViewSeq.KEY, iViewSeq);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public boolean getExist() {
		return m_bExist;
	}
	
	public void setExist(boolean bExist) {
		m_bExist = bExist;
	}
	
	public void scrollToIndex(int iIndex){
		if(m_bShow){
			JSONObject oView = new JSONObject();
			JSONObject oDo = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			try {
				oAttribute.put(HeroActionProtocol.View.Do.ScrollTo.Index.KEY, iIndex);
				
				oDo.put(HeroActionProtocol.View.Do.ScrollTo.KEY, oAttribute);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Do.KEY, oDo);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public void scrollToPosition(int iPosition){
		JSONObject oView = new JSONObject();
		JSONObject oDo = new JSONObject();
		JSONObject oAttribute = new JSONObject();
		try {
			if(iPosition == 1)
				oAttribute.put(HeroActionProtocol.View.Do.ScrollTo.Position.KEY, HeroActionProtocol.View.Do.ScrollTo.Position.TOP);
			else
			if(iPosition == 2)
				oAttribute.put(HeroActionProtocol.View.Do.ScrollTo.Position.KEY, HeroActionProtocol.View.Do.ScrollTo.Position.BOTTOM);
			
			oDo.put(HeroActionProtocol.View.Do.ScrollTo.KEY, oAttribute);
			
			oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
			oView.put(HeroActionProtocol.View.Do.KEY, oDo);
		}
		catch (Exception e) {}
		
		m_oParentTerm.appendPacket(oView);
	}
	
	public boolean isGradient(){
		return m_bGradient;
	}
	
	public void setGradient(boolean bGradient){
		// ***** In current version, disable the gradient for new UIUX until client support advance gradient display
		if (true)
			return;
		
		Boolean bNeedUpdateClient = false;
		
		if(m_bGradient != bGradient){
			m_bGradient = bGradient;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Gradient.KEY, m_bGradient);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public String getCornerRadius(){
		return m_sCornerRadius;
	}
	
	public void setCornerRadius(String sCornerRadius){
		Boolean bNeedUpdateClient = false;
		
		if(!m_sCornerRadius.equals(sCornerRadius)){
			m_sCornerRadius = sCornerRadius;
			bNeedUpdateClient = true;
		}
		
		if(m_bShow && bNeedUpdateClient){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.CornerRadius.KEY, sCornerRadius);
				
				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			m_oParentTerm.appendPacket(oView);
		}
	}
	
	public void allowClick(boolean bHaveClickEvent){
		m_bHaveClickEvent = bHaveClickEvent;
	}
	
	public boolean isAllowClick(){
		return m_bHaveClickEvent;
	}
	
	public void allowLongClick(boolean bHaveLongClickEvent){
		m_bHaveLongClickEvent = bHaveLongClickEvent;
	}
	
	public boolean isAllowLongClick(){
		return m_bHaveLongClickEvent;
	}
	
	public void allowSwipeTop(boolean bHaveSwipeTop){
		m_bHaveSwipeTopEvent = bHaveSwipeTop;
	}
	
	public boolean isAllowSwipeTop(){
		return m_bHaveSwipeTopEvent;
	}
	
	public void allowSwipeBottom(boolean bHaveSwipeBottom){
		m_bHaveSwipeBottomEvent = bHaveSwipeBottom;
	}
	
	public boolean isAllowSwipeBottom(){
		return m_bHaveSwipeBottomEvent;
	}
	
	public void allowSwipeRight(boolean bHaveSwipeRight){
		m_bHaveSwipeRightEvent = bHaveSwipeRight;
	}
	
	public boolean isAllowSwipeRight(){
		return m_bHaveSwipeRightEvent;
	}
	
	public void allowSwipeLeft(boolean bHaveSwipeLeft){
		m_bHaveSwipeLeftEvent = bHaveSwipeLeft;
	}
	
	public boolean isAllowSwipeLeft(){
		return m_bHaveSwipeLeftEvent;
	}
	
	public void allowValueChanged(boolean bHaveValueChanged){
		m_bHaveValueChangedEvent = bHaveValueChanged;
	}
	
	public boolean isAllowValueChanged(){
		return m_bHaveValueChangedEvent;
	}
	
	public void allowForward(boolean bHaveForward){
		m_bHaveForwardEvent = bHaveForward;
	}
	
	public boolean isAllowForward(){
		return m_bHaveForwardEvent;
	}
	
	public void addTimer(String sId, int iInterval, boolean bRepeat, String sNote, boolean bBlockUI, boolean bAsync, VirtualUIBasicElement oElement){
		VirtualUITimer oTimer = new VirtualUITimer();
		oTimer.setId(sId);
		oTimer.setInterval(iInterval);
		oTimer.setRepeat(bRepeat);
		oTimer.setServerRequestNote(sNote);
		oTimer.setServerRequestBlockUI(bBlockUI);
		if(oElement != null)
			oTimer.addServerRequestSubmitElement(oElement);
		oTimer.setAsync(bAsync);
		
		m_oTimerList.put(sId, oTimer);
	}
	
	public void addTimer(String sId, int iInterval, boolean bRepeat, String sNote, boolean bBlockUI, boolean bAsync, VirtualUIBasicElement oElement, int iClientTimeout){
		VirtualUITimer oTimer = new VirtualUITimer();
		oTimer.setId(sId);
		oTimer.setInterval(iInterval);
		oTimer.setRepeat(bRepeat);
		oTimer.setServerRequestNote(sNote);
		oTimer.setServerRequestBlockUI(bBlockUI);
		if(oElement != null)
			oTimer.addServerRequestSubmitElement(oElement);
		oTimer.setAsync(bAsync);
		oTimer.setClientTimeout(iClientTimeout);
		
		m_oTimerList.put(sId, oTimer);
	}
	
	public void controlTimer(String sId, boolean bStart){
		if(m_oTimerList.containsKey(sId) == false)
			return;
		
		VirtualUITimer oTimer = m_oTimerList.get(sId);
		oTimer.setEnable(bStart);
		
		// The UI is shown ==> Modify UI
		// Create packet to update UI
		if(m_bShow){
			JSONArray oJSONArray = new JSONArray();
			JSONObject oViewObject = new JSONObject();
			JSONObject oEventObject = new JSONObject();
			JSONObject oTimerObject = new JSONObject();
			try {
				oViewObject.put(HeroActionProtocol.View.Id.KEY, this.getIDForPosting());
				
				oTimerObject.put(HeroActionProtocol.View.Event.Timer.Id.KEY, sId);
				oTimerObject.put(HeroActionProtocol.View.Event.Timer.Enabled.KEY, bStart);
				
				oJSONArray.put(oTimerObject);
				
				oEventObject.put(HeroActionProtocol.View.Event.Timer.KEY, oJSONArray);
				
				oViewObject.put(HeroActionProtocol.View.Event.KEY, oEventObject);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			m_oParentTerm.appendPacket(oViewObject);
		}
	}
	
	public void updateTimerInterval(String sId, int iInterval) {
		if(m_oTimerList.containsKey(sId) == false)
			return;
		
		// The UI is shown ==> Modify UI
		// Create packet to update UI
		if (m_bShow) {
			JSONArray oJSONArray = new JSONArray();
			JSONObject oViewObject = new JSONObject();
			JSONObject oEventObject = new JSONObject();
			JSONObject oTimerObject = new JSONObject();
			try {
				oViewObject.put(HeroActionProtocol.View.Id.KEY, this.getIDForPosting());
				
				oTimerObject.put(HeroActionProtocol.View.Event.Timer.Id.KEY, sId);
				oTimerObject.put(HeroActionProtocol.View.Event.Timer.Time.KEY, iInterval);
				
				oJSONArray.put(oTimerObject);
				
				oEventObject.put(HeroActionProtocol.View.Event.Timer.KEY, oJSONArray);
				
				oViewObject.put(HeroActionProtocol.View.Event.KEY, oEventObject);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			m_oParentTerm.appendPacket(oViewObject);
		}
	}
	
	public JSONArray getTimer(){
		JSONArray oJSONArray = new JSONArray();
		JSONObject oServerRequest;
		JSONObject oJsonObject;
		
		for (Map.Entry<String, VirtualUITimer> entry : m_oTimerList.entrySet()) {
			VirtualUITimer oTimer = entry.getValue();
			oJsonObject = new JSONObject();
			oServerRequest = new JSONObject();
			try {
				oJsonObject.put(HeroActionProtocol.View.Event.Timer.Id.KEY, oTimer.getId());
				oJsonObject.put(HeroActionProtocol.View.Event.Timer.Enabled.KEY, oTimer.isEnabled());
				oJsonObject.put(HeroActionProtocol.View.Event.Timer.Time.KEY, oTimer.getInterval());
				oJsonObject.put(HeroActionProtocol.View.Event.Timer.Repeat.KEY, oTimer.isRepeat());
				
				oServerRequest.put(HeroActionProtocol.View.Event.Timer.ServerRequest.Note.KEY, oTimer.getServerRequestNote());
				if(oTimer.getServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.Timer.ServerRequest.SubmitId.KEY, oTimer.getServerRequestSubmitId());
				if(oTimer.getServerRequestBlockUI())
					oServerRequest.put(HeroActionProtocol.View.Event.Timer.ServerRequest.BlockUI.KEY, true);
				else
					oServerRequest.put(HeroActionProtocol.View.Event.Timer.ServerRequest.BlockUI.KEY, false);
				oServerRequest.put(HeroActionProtocol.View.Event.Timer.ServerRequest.Async.KEY, oTimer.getAsync());
				if(oTimer.getClientTimeout() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.Timer.ServerRequest.Timeout.KEY, oTimer.getClientTimeout());
				oJsonObject.put(HeroActionProtocol.View.Event.Timer.ServerRequest.KEY, oServerRequest);
				
				oJSONArray.put(oJsonObject);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return oJSONArray;
	}
	
	public String getIDForPosting(){
		return (m_oParentForm.getClass().getSimpleName() + "_" + m_iId);
	}
	
	private JSONObject getEventForPost(){
		JSONObject oClickEvent = new JSONObject();
		JSONArray oClickEventArray = new JSONArray();
		JSONObject oLongClickEvent = new JSONObject();
		JSONArray oLongClickEventArray = new JSONArray();
		JSONObject oSwipeTopEvent = new JSONObject();
		JSONArray oSwipeTopEventArray = new JSONArray();
		JSONObject oSwipeBottomEvent = new JSONObject();
		JSONArray oSwipeBottomEventArray = new JSONArray();
		JSONObject oSwipeRightEvent = new JSONObject();
		JSONArray oSwipeRightEventArray = new JSONArray();
		JSONObject oSwipeLeftEvent = new JSONObject();
		JSONArray oSwipeLeftEventArray = new JSONArray();
		JSONObject oEvent = new JSONObject();
		JSONObject oJSONAction = new JSONObject();
		
		try{
			if(this.isAllowClick()){
				JSONObject oServerRequest = new JSONObject();
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				
				// Client action
				oJSONAction.put(HeroActionProtocol.View.KEY, this.buildClickActionViewArray());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Action.KEY, oJSONAction);
				
				// Server action
				if(this.getClickReplaceValueCount() == 0){
					oServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.Note.KEY, this.getClickServerRequestNote());
					if(this.getClickServerRequestSubmitId().length() > 0)
						oServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.SubmitId.KEY, this.getClickServerRequestSubmitId());
					oServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.BlockUI.KEY, this.getClickServerRequestBlockUI());
					oServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.Timeout.KEY, this.getClickServerRequestTimeout());
					oClickEvent.put(HeroActionProtocol.View.Event.Click.ServerRequest.KEY, oServerRequest);
				}
				
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
			}
			
			if(this.isAllowLongClick()){
				JSONObject oServerRequest = new JSONObject();
				oLongClickEvent.put(HeroActionProtocol.View.Event.LongClick.Id.KEY, "LongClick1");
				
				oJSONAction = new JSONObject();
				oJSONAction.put(HeroActionProtocol.View.KEY, this.buildLongClickActionViewArray());
				oLongClickEvent.put(HeroActionProtocol.View.Event.LongClick.Action.KEY, oJSONAction);
				
				// Server action
				if(this.getLongClickReplaceValueCount() == 0){
					oServerRequest.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.Note.KEY, this.getLongClickServerRequestNote());
					if(this.getLongClickServerRequestSubmitId().length() > 0)
						oServerRequest.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.SubmitId.KEY, this.getLongClickServerRequestSubmitId());
					oServerRequest.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.BlockUI.KEY, this.getLongClickServerRequestBlockUI());
					oLongClickEvent.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.KEY, oServerRequest);
				}
				
				oLongClickEventArray.put(oLongClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.LongClick.KEY, oLongClickEventArray);
			}
			
			if(this.isAllowSwipeTop()){
				JSONObject oServerRequest = new JSONObject();
				oSwipeTopEvent.put(HeroActionProtocol.View.Event.SwipeTop.Id.KEY, "SwipeTop1");
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.Note.KEY, this.getSwipeTopServerRequestNote());
				if(this.getSwipeTopServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.SubmitId.KEY, this.getSwipeTopServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.BlockUI.KEY, this.getSwipeTopServerRequestBlockUI());
				oSwipeTopEvent.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.KEY, oServerRequest);
				oSwipeTopEventArray.put(oSwipeTopEvent);
				oEvent.put(HeroActionProtocol.View.Event.SwipeTop.KEY, oSwipeTopEventArray);
			}
			
			if(this.isAllowSwipeBottom()){
				JSONObject oServerRequest = new JSONObject();
				oSwipeBottomEvent.put(HeroActionProtocol.View.Event.SwipeBottom.Id.KEY, "SwipeBottom1");
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.Note.KEY, this.getSwipeBottomServerRequestNote());
				if(this.getSwipeBottomServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.SubmitId.KEY, this.getSwipeBottomServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.BlockUI.KEY, this.getSwipeBottomServerRequestBlockUI());
				oSwipeBottomEvent.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.KEY, oServerRequest);
				oSwipeBottomEventArray.put(oSwipeBottomEvent);
				oEvent.put(HeroActionProtocol.View.Event.SwipeBottom.KEY, oSwipeBottomEventArray);
			}
			
			if(this.isAllowSwipeRight()){
				JSONObject oServerRequest = new JSONObject();
				oSwipeRightEvent.put(HeroActionProtocol.View.Event.SwipeRight.Id.KEY, "SwipeRight1");
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.Note.KEY, this.getSwipeRightServerRequestNote());
				if(this.getSwipeRightServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.SubmitId.KEY, this.getSwipeRightServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.BlockUI.KEY, this.getSwipeRightServerRequestBlockUI());
				oSwipeRightEvent.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.KEY, oServerRequest);
				oSwipeRightEventArray.put(oSwipeRightEvent);
				oEvent.put(HeroActionProtocol.View.Event.SwipeRight.KEY, oSwipeRightEventArray);
			}
			
			if(this.isAllowSwipeLeft()){
				JSONObject oServerRequest = new JSONObject();
				oSwipeLeftEvent.put(HeroActionProtocol.View.Event.SwipeLeft.Id.KEY, "SwipeLeft1");
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.Note.KEY, this.getSwipeLeftServerRequestNote());
				if(this.getSwipeLeftServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.SubmitId.KEY, this.getSwipeLeftServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.BlockUI.KEY, this.getSwipeLeftServerRequestBlockUI());
				oSwipeLeftEvent.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.KEY, oServerRequest);
				oSwipeLeftEventArray.put(oSwipeLeftEvent);
				oEvent.put(HeroActionProtocol.View.Event.SwipeLeft.KEY, oSwipeLeftEventArray);
			}
			
			oEvent.put(HeroActionProtocol.View.Event.Timer.KEY, this.getTimer());
		}
		catch (Exception e) {}
		
		return oEvent;
	}
	
	public int countUIElement() {
		int iCount = 1;	// Current element
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			iCount += oBasicElement.countUIElement();
		}
		return iCount;
	}
	
	@Override
	public boolean clicked(int iId, String sNote) {
		return false;
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {		
		return false;
	}

	@Override
	public boolean longClicked(int iId, String sNote) {
		return false;
	}

	@Override
	public boolean swipeTop(int iId, String sNote) {
		return false;
	}

	@Override
	public boolean swipeBottom(int iId, String sNote) {
		return false;
	}
	
	@Override
	public boolean swipeRight(int iId, String sNote) {
		return false;
	}

	@Override
	public boolean swipeLeft(int iId, String sNote) {
		return false;
	}
	
	@Override
	public boolean valueChanged(int iId, String sNote) {
		return false;
	}
	
	@Override
	public boolean forward(int iId, String sNote, String sStatus) {
		return false;
	}
	
	@Override
	public boolean keyboard(int iId, String sNote) {
		return false;
	}
}
