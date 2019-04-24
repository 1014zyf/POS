package core.virtualui;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.Controller;
import core.Core;
import core.externallib.SparseArray;
import core.externallib.Util;
import core.manager.ActiveClient;
import core.manager.LoggingManager;

public class VirtualUIBasicElement {
	public static final VirtualUIBasicElement DEFAULT_PARENT_ELEMENT = null;
	public static final int DEFAULT_ID = 0;
	public static final String DEFAULT_TYPE = "";
	public static final VirtualUIBasicElement DEFAULT_VALUE_MIRROR_ELEMENT = null;
	public static final int DEFAULT_WIDTH = 0;
	public static final int DEFAULT_HEIGHT = 0;
	public static final int DEFAULT_CONTENT_WIDTH = 0;
	public static final int DEFAULT_CONTENT_HEIGHT = 0;
	public static final int DEFAULT_TOP = 0;
	public static final int DEFAULT_LEFT = 0;
	public static final String DEFAULT_VALUE = "";
	public static final String DEFAULT_BACKGROUND_COLOR = "";
	public static final boolean DEFAULT_GRADIENT = false;
	public static final int DEFAULT_CORNER_RADII = 0;
	public static final int DEFAULT_STROKE = 0;
	public static final String DEFAULT_STROKE_COLOR = "";
	public static final String DEFAULT_FOREGROUND_COLOR = "";
	public static final String DEFAULT_TEXT_ALIGN = "";
	public static final String DEFAULT_TEXT_STYLE = "";
	public static final int DEFAULT_TEXT_SIZE = 12;
	public static final boolean DEFAULT_VISIBLE = true;
	public static final boolean DEFAULT_ENABLED = true;
	public static final int DEFAULT_VIEW_SEQ = 0;
	public static final String DEFAULT_INPUT_TYPE = "";
	public static final String DEFAULT_KEYBOARD_TYPE = "";
	public static final boolean DEFAULT_MULTILINE = false;
	public static final String DEFAULT_SOURCE = "";
	public static final String DEFAULT_CONTENT_MODE = "";
	public static final String DEFAULT_HINT = "";
	public static final int DEFAULT_PADDING = 0;
	
	private Controller m_oController;	//	Only Controller's root element has value
	private int m_iVersion;
	
	// Flag to determine if the element is shown by calling parent form.showAndWait()
//	protected boolean m_bIsShow;
//	protected boolean m_bIsEdited;
	protected boolean m_bIsFullEdited;
	
	// Flag to determine if the element is deleted or not
	protected boolean m_bDeleted;
	protected boolean m_bDoDeletedChildren;
	
	protected VirtualUIDoReplaceValue m_oDoReplaceValue;
	protected boolean m_bDoTop;	//	####### Can be optimize to unique level
	protected boolean m_bDoFocus;	//	####### Can be optimize to unique level
	protected VirtualUIDoScrollTo m_oDoScrollTo;	//	####### Can be optimize to ELEMENT unique level
	protected VirtualUIDoTriggerEvent m_oDoTriggerEvent;
	
	// Parent element
	protected VirtualUIBasicElement m_oParentElement;		protected VirtualUIBasicElement m_oParentElementEdited;
	
	// List of child basic elements
	protected ArrayList<VirtualUIBasicElement> m_oChildElementList;
	
	protected int m_iId;
	protected String m_sType;
	protected VirtualUIBasicElement m_oValueMirrorElement;		protected VirtualUIBasicElement m_oValueMirrorElementEdited;
	
	// Attribute
	protected int m_iWidth;					protected int m_iWidthEdited;
	protected int m_iHeight;				protected int m_iHeightEdited;
	protected int m_iContentWidth;			protected int m_iContentWidthEdited;
	protected int m_iContentHeight;			protected int m_iContentHeightEdited;
	protected int m_iTop;					protected int m_iTopEdited;
	protected int m_iLeft;					protected int m_iLeftEdited;
	protected String m_sValue;				protected String m_sValueEdited;
	protected String m_sBackgroundColor;	protected String m_sBackgroundColorEdited;
	protected boolean m_bGradient;			protected boolean m_bGradientEdited;
	protected int[] m_iCornerRadii;			protected int[] m_iCornerRadiiEdited;
	protected int m_iStroke;				protected int m_iStrokeEdited;
	protected String m_sStrokeColor;		protected String m_sStrokeColorEdited;
	protected String m_sForegroundColor;	protected String m_sForegroundColorEdited;
	protected String m_sTextAlign;			protected String m_sTextAlignEdited;
	protected String m_sTextStyle;			protected String m_sTextStyleEdited;
	protected int m_iTextSize;				protected int m_iTextSizeEdited;
	protected boolean m_bVisible;			protected boolean m_bVisibleEdited;
	protected boolean m_bEnabled;			protected boolean m_bEnabledEdited;
	protected int m_iViewSeq;				protected int m_iViewSeqEdited;
	protected String m_sInputType;			protected String m_sInputTypeEdited;
	protected String m_sKeyboardType;		protected String m_sKeyboardTypeEdited;
	protected boolean m_bMultiline;			protected boolean m_bMultilineEdited;
	protected String m_sSource;				protected String m_sSourceEdited;
	protected String m_sContentMode;		protected String m_sContentModeEdited;
	protected String m_sHint;				protected String m_sHintEdited;
	
	// Attribute (virtual)
	protected int[] m_iPadding;			protected int[] m_iPaddingEdited;
	
	// Events
	protected SparseArray<VirtualUIEvent> m_oEvents;
	protected SparseArray<VirtualUIEvent> m_oEventsEdited;
	
	//	Server side variables
	protected Object m_oTag;
	
	// For compatible
	// Flag to determine if the element is posted to client or not
	protected boolean m_bExist;
	
	protected boolean m_bIsRootElement;
	
	public VirtualUIBasicElement(String sType) {
		construct(sType, DEFAULT_ID);
	}
	
	public VirtualUIBasicElement(String sType, int iPreservedId) {
		construct(sType, iPreservedId);
	}
	
	// Constructor
	public void construct(String sType, int iPreservedId){
		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		
		// False until parent form is shown 
//		m_bIsShow = false;
//		m_bIsEdited = true;
		m_bIsFullEdited = true;
		m_bIsRootElement = false;
		
		// Get a unique ID
		if (iPreservedId == DEFAULT_ID)
			m_iId = oActiveClient.g_oUIManager.getElementId();
		else
			m_iId = iPreservedId;
		m_sType = sType;
		
		m_bDeleted = false;
		m_bDoDeletedChildren = false;
		m_bExist = false;
		m_bDoTop = false;
		m_bDoFocus = false;
		m_oDoReplaceValue = null;
		m_oDoScrollTo = null;
		m_oDoTriggerEvent = null;
		
		m_oChildElementList = new ArrayList<VirtualUIBasicElement>();
		
		m_oParentElement = m_oParentElementEdited = DEFAULT_PARENT_ELEMENT;
		m_oValueMirrorElement = m_oValueMirrorElementEdited = DEFAULT_VALUE_MIRROR_ELEMENT;
		m_iWidth = m_iWidthEdited = DEFAULT_WIDTH;
		m_iHeight = m_iHeightEdited = DEFAULT_HEIGHT;
		m_iContentWidth = m_iContentWidthEdited = DEFAULT_CONTENT_WIDTH;
		m_iContentHeight = m_iContentHeightEdited = DEFAULT_CONTENT_HEIGHT;
		m_iTop = m_iTopEdited = DEFAULT_TOP;
		m_iLeft = m_iLeftEdited = DEFAULT_LEFT;
		m_sValue = m_sValueEdited = DEFAULT_VALUE;
		m_sBackgroundColor = m_sBackgroundColorEdited = DEFAULT_BACKGROUND_COLOR;
		m_bGradient = m_bGradientEdited = DEFAULT_GRADIENT;
		m_iCornerRadii = new int[4];	m_iCornerRadiiEdited = new int[4];
		Arrays.fill(m_iCornerRadii, DEFAULT_CORNER_RADII);
		Arrays.fill(m_iCornerRadiiEdited, DEFAULT_CORNER_RADII);
		m_iStroke = m_iStrokeEdited = DEFAULT_STROKE;
		m_sStrokeColor = m_sStrokeColorEdited = DEFAULT_STROKE_COLOR;
		m_sForegroundColor = m_sForegroundColorEdited = DEFAULT_FOREGROUND_COLOR;
		m_sTextAlign = m_sTextAlignEdited = DEFAULT_TEXT_ALIGN;
		m_sTextStyle = m_sTextStyleEdited = DEFAULT_TEXT_STYLE;
		m_iTextSize = m_iTextSizeEdited = DEFAULT_TEXT_SIZE;
		m_bVisible = m_bVisibleEdited = DEFAULT_VISIBLE;
		m_bEnabled = m_bEnabledEdited = DEFAULT_ENABLED;
		m_iViewSeq = m_iViewSeqEdited = DEFAULT_VIEW_SEQ;
		m_sInputType = m_sInputTypeEdited = DEFAULT_INPUT_TYPE;
		m_sKeyboardType = m_sKeyboardTypeEdited = DEFAULT_KEYBOARD_TYPE;
		m_bMultiline = m_bMultilineEdited = DEFAULT_MULTILINE;
		m_sSource = m_sSourceEdited = DEFAULT_SOURCE;
		m_sContentMode = m_sContentModeEdited = DEFAULT_CONTENT_MODE;
		m_sHint = m_sHintEdited = DEFAULT_HINT;
		
		m_iPadding = new int[4];	m_iPaddingEdited = new int[4];
		Arrays.fill(m_iPadding, DEFAULT_PADDING);
		Arrays.fill(m_iPaddingEdited, DEFAULT_PADDING);
		
		m_oEvents = new SparseArray<VirtualUIEvent>();			m_oEventsEdited = new SparseArray<VirtualUIEvent>();
//		m_oLongClickEvents = new LinkedHashMap<String, VirtualUIEvent>();		m_oLongClickEventsEdited = new LinkedHashMap<String, VirtualUIEvent>();
//		m_oSwipeRightEvents = new LinkedHashMap<String, VirtualUIEvent>();		m_oSwipeRightEventsEdited = new LinkedHashMap<String, VirtualUIEvent>();
//		m_oSwipeLeftEvents = new LinkedHashMap<String, VirtualUIEvent>();		m_oSwipeLeftEventsEdited = new LinkedHashMap<String, VirtualUIEvent>();
//		m_oSwipeTopEvents = new LinkedHashMap<String, VirtualUIEvent>();		m_oSwipeTopEventsEdited = new LinkedHashMap<String, VirtualUIEvent>();
//		m_oSwipeBottomEvents = new LinkedHashMap<String, VirtualUIEvent>();		m_oSwipeBottomEventsEdited = new LinkedHashMap<String, VirtualUIEvent>();
//		m_oValueChangedEvents = new LinkedHashMap<String, VirtualUIEvent>();	m_oValueChangedEventsEdited = new LinkedHashMap<String, VirtualUIEvent>();
//		m_oTimerEvents = new LinkedHashMap<String, VirtualUIEvent>();			m_oTimerEventsEdited = new LinkedHashMap<String, VirtualUIEvent>();
//		m_oIdleEvents = new LinkedHashMap<String, VirtualUIEvent>();			m_oIdleEventsEdited = new LinkedHashMap<String, VirtualUIEvent>();
		
		m_oTag = null;
	}
	
	public void setController(Controller oController) {
		m_oController = oController;
	}
	
	public void setIsRootElement(boolean bIsRootElement) {
		m_bIsRootElement = bIsRootElement;
		
		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		oActiveClient.g_oUIManager.addElement(this);
	}
	
	public boolean isRootElement() {
		return m_bIsRootElement;
	}
	
	public void applyEdit() {
//		m_bIsEdited = false;
		m_bIsFullEdited = false;
		
		m_bDeleted = false;
		m_bDoDeletedChildren = false;
		m_bDoTop = false;
		m_bDoFocus = false;
		m_oDoReplaceValue = null;
		m_oDoScrollTo = null;
		m_oDoTriggerEvent = null;
		
		m_oParentElement = m_oParentElementEdited;
		m_oValueMirrorElement = m_oValueMirrorElementEdited;
		m_iWidth = m_iWidthEdited;
		m_iHeight = m_iHeightEdited;
		m_iContentWidth = m_iContentWidthEdited;
		m_iContentHeight = m_iContentHeightEdited;
		m_iTop = m_iTopEdited;
		m_iLeft = m_iLeftEdited;
		m_sValue = m_sValueEdited;
		m_sBackgroundColor = m_sBackgroundColorEdited;
		m_bGradient = m_bGradientEdited;
		System.arraycopy(m_iCornerRadiiEdited, 0, m_iCornerRadii, 0, m_iCornerRadii.length);
		m_iStroke = m_iStrokeEdited;
		m_sStrokeColor = m_sStrokeColorEdited;
		m_sForegroundColor = m_sForegroundColorEdited;
		m_sTextAlign = new String(m_sTextAlignEdited);
		m_sTextStyle = new String(m_sTextStyleEdited);
		m_iTextSize = m_iTextSizeEdited;
		m_bVisible = m_bVisibleEdited;
		m_bEnabled = m_bEnabledEdited;
		m_iViewSeq = m_iViewSeqEdited;
		m_sInputType = new String(m_sInputTypeEdited);
		m_sKeyboardType = new String(m_sKeyboardTypeEdited);
		m_bMultiline = m_bMultilineEdited;
		m_sSource = new String(m_sSourceEdited);
		m_sContentMode = new String(m_sContentModeEdited);
		m_sHint = new String(m_sHintEdited);

		System.arraycopy(m_iPaddingEdited, 0, m_iPadding, 0, m_iPadding.length);
		
		m_oEvents.clear();
		VirtualUIEvent oEvent = null;
		for (int i = 0; i < m_oEventsEdited.size(); i++) {
			oEvent = m_oEventsEdited.valueAt(i);
			oEvent.applyEdit();
			m_oEvents.append(oEvent.getId(), oEvent);
		}
	}
	
	public boolean isFullEdited() {
		return m_bIsFullEdited;
	}
	
	public void setEdited() {
		setEdited(false);
	}
	
	public void setEdited(boolean full) {
		
//		m_bIsEdited = true;
		
//		if (m_bIsShow) {
			m_bIsFullEdited |= full;
			Core.g_oClientManager.getActiveClient().g_oUIManager.setElementEdited(this);
//		}
	}
	
	public void setTag(Object obj) {
		m_oTag = obj;
	}
	
	public Object getTag() {
		return m_oTag;
	}
	
	public int getVersion() {
		return m_iVersion;
	}
	
//	public boolean isEdited() {
//		return m_bIsEdited;
//	}
	
	public JSONObject buildElementJsonObject(){
		// Create packet to update UI
		JSONObject oViewJsonObject = new JSONObject();
		
		if (!m_bIsRootElement && m_oParentElementEdited == null)
			return oViewJsonObject;
		
//		if (!full && !m_bIsEdited)
//			return oViewJsonObject;
		
		// If element is not necessary to post to client, skip the function
		if (!m_bExist)
			return oViewJsonObject;
		
		try {
			//	Id
			oViewJsonObject.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
			
			//	Version increment
			ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();	//	############################# prevent using same increment id which cause thread lock
			m_iVersion = oActiveClient.g_oUIManager.getElementId();
			
			if (m_bDeleted) {
				JSONObject oDoJsonObject = new JSONObject();
				oDoJsonObject.put(HeroActionProtocol.View.Do.Remove.KEY, new JSONObject());
				oViewJsonObject.put(HeroActionProtocol.View.Do.KEY, oDoJsonObject);
			}
			else {
				//	Type
				if (m_bIsFullEdited)	//	########### temp
					oViewJsonObject.put(HeroActionProtocol.View.Type.KEY, m_sType);
				//	Parent Id
				if ((m_bIsFullEdited && m_oParentElementEdited != DEFAULT_PARENT_ELEMENT) || (!m_bIsFullEdited && m_oParentElementEdited != m_oParentElement)) {
					oViewJsonObject.put(HeroActionProtocol.View.ParentId.KEY, m_oParentElementEdited.getIDForPosting());
				}
				else if (m_bIsFullEdited && m_oParentElementEdited == DEFAULT_PARENT_ELEMENT) {
					//	Not assign parent yet ##### temp handle UI create before parent UI
					oViewJsonObject.put(HeroActionProtocol.View.ParentId.KEY, HeroActionProtocol.View.Id.ROOT);
				}
				else if (!m_bIsFullEdited && (m_oParentElementEdited != m_oParentElement && m_oParentElementEdited == null)) {
					//	1. Not resuming
					//	2. Parent changed
					//	3. New parent is root (null)
					oViewJsonObject.put(HeroActionProtocol.View.ParentId.KEY, HeroActionProtocol.View.Id.ROOT);
				}
				
				//	Value Mirror Id ############### not implemeneted
				if (m_bIsFullEdited || (m_oValueMirrorElementEdited != m_oValueMirrorElement)) {
					if (m_oValueMirrorElementEdited != DEFAULT_VALUE_MIRROR_ELEMENT)
						oViewJsonObject.put(HeroActionProtocol.View.ValueMirrorId.KEY, m_oValueMirrorElementEdited.getIDForPosting());
					else if (m_oValueMirrorElement != DEFAULT_VALUE_MIRROR_ELEMENT)
						oViewJsonObject.put(HeroActionProtocol.View.ValueMirrorId.KEY, "");
				}
				
				JSONObject oAttributeJsonObject = new JSONObject();
				
				//	Attribute
				//	Width
				if (m_bIsFullEdited || m_iWidthEdited != m_iWidth) {
					if ((m_bIsFullEdited && m_iWidthEdited != DEFAULT_WIDTH) || (!m_bIsFullEdited && m_iWidthEdited != m_iWidth))
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Width.KEY, m_iWidthEdited);
				}
				//	Height
				if (m_bIsFullEdited || m_iHeightEdited != m_iHeight) {
					if ((m_bIsFullEdited && m_iHeightEdited != DEFAULT_HEIGHT) || (!m_bIsFullEdited && m_iHeightEdited != m_iHeight))
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Height.KEY, m_iHeightEdited);
				}
				//	Content Width
				if (m_bIsFullEdited || m_iContentWidthEdited != m_iContentWidth) {
					if ((m_bIsFullEdited && m_iContentWidthEdited != DEFAULT_CONTENT_WIDTH) || (!m_bIsFullEdited && m_iContentWidthEdited != m_iContentWidth))
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.ContentWidth.KEY, m_iContentWidthEdited);
				}
				//	Content Height
				if (m_bIsFullEdited || m_iContentHeightEdited != m_iContentHeight) {
					if ((m_bIsFullEdited && m_iContentHeightEdited != DEFAULT_CONTENT_HEIGHT) || (!m_bIsFullEdited && m_iContentHeightEdited != m_iContentHeight))
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.ContentHeight.KEY, m_iContentHeightEdited);
				}
				//	Top
				if (m_bIsFullEdited || m_iTopEdited != m_iTop) {
					if ((m_bIsFullEdited && m_iTopEdited != DEFAULT_TOP) || (!m_bIsFullEdited && m_iTopEdited != m_iTop))
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Top.KEY, m_iTopEdited);
				}
				//	Left
				if (m_bIsFullEdited || m_iLeftEdited != m_iLeft) {
					if ((m_bIsFullEdited && m_iLeftEdited != DEFAULT_LEFT) || (!m_bIsFullEdited && m_iLeftEdited != m_iLeft))
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Left.KEY, m_iLeftEdited);
				}
				//	Padding
				if ((m_bIsFullEdited && !Util.isArrayIntAllEqual(m_iPaddingEdited, DEFAULT_PADDING)) || (!m_bIsFullEdited && !Arrays.equals(m_iPaddingEdited, m_iPadding))) {
					boolean bUnifiedPadding = true;
					for (int i = 1; i < m_iPaddingEdited.length; i++) {
						if (m_iPaddingEdited[0] != m_iPaddingEdited[i]) {
							bUnifiedPadding = false;
							break;
						}
					}
					if (bUnifiedPadding)
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Padding.KEY, m_iPaddingEdited[0]);
					else
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Padding.KEY, Util.intGlue(m_iPaddingEdited, ","));
				}
				//	Value
				if ((m_bIsFullEdited && !m_sValueEdited.contentEquals(DEFAULT_VALUE)) || (!m_bIsFullEdited && !m_sValueEdited.contentEquals(m_sValue)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Value.KEY, m_sValueEdited);
				//	Background
				if ((m_bIsFullEdited && !m_sBackgroundColorEdited.contentEquals(DEFAULT_BACKGROUND_COLOR)) || (!m_bIsFullEdited && !m_sBackgroundColorEdited.contentEquals(m_sBackgroundColor)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Background.KEY, m_sBackgroundColorEdited);
				//	Gradient
				if ((m_bIsFullEdited && m_bGradientEdited) || (!m_bIsFullEdited && m_bGradientEdited != m_bGradient))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Gradient.KEY, m_bGradientEdited);
				//	Corner Radius
				if ((m_bIsFullEdited && !Util.isArrayIntAllEqual(m_iCornerRadiiEdited, DEFAULT_CORNER_RADII)) || (!m_bIsFullEdited && !Arrays.equals(m_iCornerRadiiEdited, m_iCornerRadii))) {
					boolean bUnifiedCornerRadii = true;
					for (int i = 1; i < m_iCornerRadiiEdited.length; i++) {
						if (m_iCornerRadiiEdited[0] != m_iCornerRadiiEdited[i]) {
							bUnifiedCornerRadii = false;
							break;
						}
					}
					if (bUnifiedCornerRadii)
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.CornerRadius.KEY, m_iCornerRadiiEdited[0]);
					else
						oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.CornerRadius.KEY, Util.intGlue(m_iCornerRadiiEdited, ","));
				}
				//	Stroke
				if ((m_bIsFullEdited && m_iStrokeEdited != DEFAULT_STROKE) || (!m_bIsFullEdited && m_iStrokeEdited != m_iStroke))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Stroke.KEY, m_iStrokeEdited);
				//	Stroke Color
				if ((m_bIsFullEdited && !m_sStrokeColorEdited.contentEquals(DEFAULT_STROKE_COLOR)) || (!m_bIsFullEdited && !m_sStrokeColorEdited.contentEquals(m_sStrokeColor)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.StrokeColor.KEY, m_sStrokeColorEdited);
				//	Foreground Color
				if ((m_bIsFullEdited && !m_sForegroundColorEdited.contentEquals(DEFAULT_FOREGROUND_COLOR)) || (!m_bIsFullEdited && !m_sForegroundColorEdited.contentEquals(m_sForegroundColor)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Foreground.KEY, m_sForegroundColorEdited);
				//	Text Align
				if ((m_bIsFullEdited && !m_sTextAlignEdited.contentEquals(DEFAULT_TEXT_ALIGN)) || (!m_bIsFullEdited && !m_sTextAlignEdited.contentEquals(m_sTextAlign)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.TextAlign.KEY, m_sTextAlignEdited);
				//	Text Style
				if ((m_bIsFullEdited && !m_sTextStyleEdited.contentEquals(DEFAULT_TEXT_STYLE)) || (!m_bIsFullEdited && !m_sTextStyleEdited.contentEquals(m_sTextStyle)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.TextStyle.KEY, m_sTextStyleEdited);
				//	Text size
				if ((m_bIsFullEdited && m_iTextSizeEdited != DEFAULT_TEXT_SIZE) || (!m_bIsFullEdited && m_iTextSizeEdited != m_iTextSize))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.TextSize.KEY, m_iTextSizeEdited);
				//	Visible
				if ((m_bIsFullEdited && m_bVisibleEdited != DEFAULT_VISIBLE) || (!m_bIsFullEdited && m_bVisibleEdited != m_bVisible))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Visible.KEY, m_bVisibleEdited);
				//	Enabled
				if ((m_bIsFullEdited && m_bEnabledEdited != DEFAULT_ENABLED) || (!m_bIsFullEdited && m_bEnabledEdited != m_bEnabled))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Enabled.KEY, m_bEnabledEdited);
				//	ViewSeq
				if ((m_bIsFullEdited && m_iViewSeqEdited != DEFAULT_VIEW_SEQ) || (!m_bIsFullEdited && m_iViewSeqEdited != m_iViewSeq))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.ViewSeq.KEY, m_iViewSeqEdited);
				//	InputType
				if ((m_bIsFullEdited && !m_sInputTypeEdited.contentEquals(DEFAULT_INPUT_TYPE)) || (!m_bIsFullEdited && !m_sInputTypeEdited.contentEquals(m_sInputType)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.InputType.KEY, m_sInputTypeEdited);
				//	KeyboardType
				if ((m_bIsFullEdited && !m_sKeyboardTypeEdited.contentEquals(DEFAULT_KEYBOARD_TYPE)) || (!m_bIsFullEdited && !m_sKeyboardTypeEdited.contentEquals(m_sKeyboardType)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.KeyboardType.KEY, m_sKeyboardTypeEdited);
				//	Multiline
				if ((m_bIsFullEdited && m_bMultilineEdited != DEFAULT_MULTILINE) || (!m_bIsFullEdited && m_bMultilineEdited != m_bMultiline))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Multiline.KEY, m_bMultilineEdited);
				//	Source
				if ((m_bIsFullEdited && !m_sSourceEdited.contentEquals(DEFAULT_SOURCE)) || (!m_bIsFullEdited && !m_sSourceEdited.contentEquals(m_sSource)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Source.KEY, m_sSourceEdited);
				//	ContentMode
				if ((m_bIsFullEdited && !m_sContentModeEdited.contentEquals(DEFAULT_CONTENT_MODE)) || (!m_bIsFullEdited && !m_sContentModeEdited.contentEquals(m_sContentMode)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.ContentMode.KEY, m_sContentModeEdited);
				//	Hint
				if ((m_bIsFullEdited && !m_sHintEdited.contentEquals(DEFAULT_HINT)) || (!m_bIsFullEdited && !m_sHintEdited.contentEquals(m_sHint)))
					oAttributeJsonObject.put(HeroActionProtocol.View.Attribute.Hint.KEY, m_sHintEdited);
				
				//	Check if there are attribute updated
				if (oAttributeJsonObject.length() > 0)
					oViewJsonObject.put(HeroActionProtocol.View.Attribute.KEY, oAttributeJsonObject);
				
				//	Events
				JSONObject oEventJsonObject = new JSONObject(); 
				for (String eventType : VirtualUIEvent.EVENT_TYPES) {
					JSONArray oEventJsonArray = buildElementEventJsonArray(eventType, m_bIsFullEdited);
					if (oEventJsonArray.length() == 0)
						continue;
					String eventTypeKey = HeroActionProtocol.View.Event.Click.KEY;
					if (eventType.contentEquals(VirtualUIEvent.EVENT_CLICK))
						eventTypeKey = HeroActionProtocol.View.Event.Click.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_LONG_CLICK))
						eventTypeKey = HeroActionProtocol.View.Event.LongClick.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_SWIPE_RIGHT))
						eventTypeKey = HeroActionProtocol.View.Event.SwipeRight.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_SWIPE_LEFT))
						eventTypeKey = HeroActionProtocol.View.Event.SwipeLeft.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_SWIPE_TOP))
						eventTypeKey = HeroActionProtocol.View.Event.SwipeTop.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_SWIPE_BOTTOM))
						eventTypeKey = HeroActionProtocol.View.Event.SwipeBottom.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_VALUE_CHANGED))
						eventTypeKey = HeroActionProtocol.View.Event.ValueChanged.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_KEYBOARD))
						eventTypeKey = HeroActionProtocol.View.Event.Keyboard.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_TIMER))
						eventTypeKey = HeroActionProtocol.View.Event.Timer.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_IDLE))
						eventTypeKey = HeroActionProtocol.View.Event.Idle.KEY;
					else if (eventType.contentEquals(VirtualUIEvent.EVENT_FORWARD))
						eventTypeKey = HeroActionProtocol.View.Event.Forward.KEY;
					oEventJsonObject.put(eventTypeKey, oEventJsonArray);
				}
				//	Check if there are event updated
				if (oEventJsonObject.length() > 0)
					oViewJsonObject.put(HeroActionProtocol.View.Event.KEY, oEventJsonObject);
				
				JSONObject oDoJsonObject = new JSONObject();
				
				if (m_bDoDeletedChildren) {
					oDoJsonObject.put(HeroActionProtocol.View.Do.RemoveChildren.KEY, new JSONObject());
				}
				
				if (m_oDoReplaceValue != null) {
					JSONObject oDoReplaceValueJsonObject = m_oDoReplaceValue.buildEventJsonObject();
					
					if (oDoReplaceValueJsonObject.length() > 0)
						oDoJsonObject.put(HeroActionProtocol.View.Do.ReplaceValue.KEY, oDoReplaceValueJsonObject);
				}
				
				if (m_oDoScrollTo != null) {
					JSONObject oDoScrollToJsonObject = m_oDoScrollTo.buildEventJsonObject();
					
					if (oDoScrollToJsonObject.length() > 0)
						oDoJsonObject.put(HeroActionProtocol.View.Do.ScrollTo.KEY, oDoScrollToJsonObject);
				}
				
				if (m_bDoFocus) {
					oDoJsonObject.put(HeroActionProtocol.View.Do.Focus.KEY, new JSONObject());
				}
				
				if (m_bDoTop) {
					oDoJsonObject.put(HeroActionProtocol.View.Do.Top.KEY, new JSONObject());
				}
				
				if (m_oDoTriggerEvent != null) {
					JSONObject oDoTiggerEventJsonObject = m_oDoTriggerEvent.buildEventJsonObject();
					
					if (oDoTiggerEventJsonObject.length() > 0)
						oDoJsonObject.put(HeroActionProtocol.View.Do.TriggerEvent.KEY, oDoTiggerEventJsonObject);
				}
				
				//	Check if there are DO updated
				if (oDoJsonObject.length() > 0)
					oViewJsonObject.put(HeroActionProtocol.View.Do.KEY, oDoJsonObject);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return oViewJsonObject;
	}
	
	private JSONArray buildElementEventJsonArray(String eventType, boolean full) throws JSONException {
		//	Basic events
		JSONArray oEventJsonArray = new JSONArray();
		
		//	Check cancelled
		for (int i = 0; i < m_oEvents.size(); i++) {
			VirtualUIEvent oEvent = m_oEvents.valueAt(i);
			if (!oEvent.getType().contentEquals(eventType))
				continue;
			
			if (m_oEventsEdited.indexOfKey(oEvent.getId()) < 0) {
				//	Cancelled
				JSONObject oEventJsonObject = new JSONObject();
				oEventJsonObject.put(HeroActionProtocol.View.Event.Click.Id.KEY, oEvent.getId());
				oEventJsonObject.put(HeroActionProtocol.View.Event.Click.Cancel.KEY, true);
				
				oEventJsonArray.put(oEventJsonObject);
			}
		}
		
		//	Check edited
		for (int i = 0; i < m_oEventsEdited.size(); i++) {
			VirtualUIEvent oEvent = m_oEventsEdited.valueAt(i);
			if (!oEvent.getType().contentEquals(eventType))
				continue;
			
			if (full || oEvent.isEdited()) {
				JSONObject oEventJson = oEvent.buildEventJsonObject(full);
				if (oEventJson.length() > 0)
					oEventJsonArray.put(oEventJson);
			}
		}
		
		return oEventJsonArray;
	}
	
	public void show(){
//		if (m_bIsShow)
//			return;
		
//		m_bIsShow = true;
		
		// Set the element to be edited
		setEdited();
		
		// Show children
		for(VirtualUIBasicElement oElement : m_oChildElementList){
			oElement.show();
		}
	}
	
	public boolean isShow(){
		return true;
	}
	
	public void attachChild(VirtualUIBasicElement oElement){
		//	check if already attached
		if (oElement.m_oParentElementEdited == this) {
			oElement.top();
			return;
		}
		else if (oElement.m_oParentElementEdited != null) {
			//	################### cannot switch parent
			return;
		}
		
		// Add child 
		m_oChildElementList.add(oElement);
		
		boolean bIsFirstSetParent = (oElement.m_oParentElementEdited == null);
		// Set parent to child element
		oElement.m_oParentElementEdited = this;
		
		// Add element to UIManager element pool
		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		oActiveClient.g_oUIManager.addElement(oElement);
		
		// Set the element to be edited
		setEdited();
		if (bIsFirstSetParent) {
			for (VirtualUIBasicElement oChildElement : getAllChildSet())
				oChildElement.setEdited();
		}
    }
	
	public void removeMyself(){
		//	Remove all children
		removeAllChildren();
		
		// Mark delete
		m_bDeleted = true;
		
		// Remove myself from parent element
		// *** except the frame's parent is form (controller)
		if(m_oParentElementEdited != null)
			m_oParentElementEdited.removeChildRelationship(this);

		UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
		
		// Set the element to be edited
		setEdited();
		
		// Remove the element from the element pool of UI Manager
		oUIManager.removeElement(this);
		
		//	Apply immediately
		oUIManager.applyAllEdit();	//	############### can be optimize
	}
	
	public void removeAllChildren(){
		if (m_oChildElementList.isEmpty())
			return;
		
		m_bDoDeletedChildren = true;
		
		UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
		
		// Set the element to be edited
		setEdited();
		
		ArrayList<VirtualUIBasicElement> oAllChildSet = getAllChildSet();
		
		// Set all children to be ignore edited
		oUIManager.setElementIgnoreEdited(oAllChildSet);
		
		// Remove the element from the element pool of UI Manager
		oUIManager.removeElement(oAllChildSet);
		
		m_oChildElementList.clear();
		
		//	Apply immediately
		oUIManager.applyAllEdit();	//	############### can be optimize
    }
	
	public void removeChild(int iElementId){	//	####################### will remove
		UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
		
		VirtualUIBasicElement oElement = oUIManager.getElement(iElementId);
		if (oElement == null)
			return;
		
		oElement.removeMyself();
    }
	
	public void setFocusWhenShow(boolean bFocus) {	//	################## will remove
		if (bFocus)
			focus();
	}
	
	public void focus() {
		if (m_bDoFocus)
			return;
		
		m_bDoFocus = true;
		
		if (isShow()) {
			UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
			
			// Set the element to be edited
			setEdited();
			
			//	Apply immediately
			oUIManager.applyAllEdit();	//	############### can be optimize
		}
    }
	
	public void top() {	//	####################### to be private
		if (m_oParentElementEdited == null)
			return;
		
		m_oParentElementEdited.m_oChildElementList.remove(this);
		m_oParentElementEdited.m_oChildElementList.add(this);
		
		if (isShow()) {
//			if (m_bDoTop)	//	May top again
//				return;
			
			//Sunny
			ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
			if (oActiveClient == null) {
				System.out.println("testtest ====================");
			}
			
			UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
			
			//	Apply immediately	//	add all new child first
			oUIManager.applyAllEdit();	//	############### can be optimize
			
			m_bDoTop = true;
			m_bVisibleEdited = true;	//	################### to be remove
			m_bEnabledEdited = true;	//	################### to be remove
			
			// Set the element to be edited
			setEdited();
		
			//	Apply immediately
			oUIManager.applyAllEdit();	//	############### can be optimize
		}
    }
	
	public void scrollToTop() {
		m_oDoScrollTo = new VirtualUIDoScrollTo();
		m_oDoScrollTo.toTop();

		if (isShow()) {
			UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
			
			// Set the element to be edited
			setEdited();
		
			//	Apply immediately
			oUIManager.applyAllEdit();	//	############### can be optimize
		}
	}
	
	public void scrollToBottom() {
		m_oDoScrollTo = new VirtualUIDoScrollTo();
		m_oDoScrollTo.toBottom();

		if (isShow()) {
			UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
			
			// Set the element to be edited
			setEdited();
		
			//	Apply immediately
			oUIManager.applyAllEdit();	//	############### can be optimize
		}
	}
	
	public void scrollToIndex(int iIndex) {
		m_oDoScrollTo = new VirtualUIDoScrollTo();
		m_oDoScrollTo.toIndex(iIndex);

		if (isShow()) {
			UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
			
			// Set the element to be edited
			setEdited();
		
			//	Apply immediately
			oUIManager.applyAllEdit();	//	############### can be optimize
		}
	}
	
	public void replaceValue(String sReplaceValueRegex, String sReplaceValue) {
		m_oDoReplaceValue = new VirtualUIDoReplaceValue();
		m_oDoReplaceValue.setRegex(sReplaceValueRegex);
		m_oDoReplaceValue.setValue(sReplaceValue);

		if (isShow()) {
			UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
			
			// Set the element to be edited
			setEdited();
		
			//	Apply immediately
			oUIManager.applyAllEdit();	//	############### can be optimize
		}
	}
	
	public void doAutoClick(int iDelay) {	//	#################### to be remove
		triggerClick(iDelay);
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
	
	protected void triggerEvent(String sEvent, int iDelay) {
		m_oDoTriggerEvent = new VirtualUIDoTriggerEvent(sEvent, iDelay);

		if (isShow()) {
			UIManager oUIManager = Core.g_oClientManager.getActiveClient().g_oUIManager;
			
			// Set the element to be edited
			setEdited();
		
			//	Apply immediately
			oUIManager.applyAllEdit();	//	############### can be optimize
		}
	}
	
	//	Call when child remove itself
	protected void removeChildRelationship(VirtualUIBasicElement oElement){
		m_oChildElementList.remove(oElement);
    }
	
//	public int getPrimaryParent(){
//		if(m_oParentElement == null)
//			return this.getId();
//		else
//			return m_oParentElement.getPrimaryParent();
//	}
	
	public void doEvent(int iId, String eventType) {
		VirtualUIEvent oEvent = getEventByType(eventType);
		if (oEvent == null)
			return;
		if (eventType.equals(VirtualUIEvent.EVENT_CLICK)) {
			clicked(iId, oEvent.getNote());
			clicked(iId);
		}
		else if (eventType.equals(VirtualUIEvent.EVENT_LONG_CLICK)) {
			longClicked(iId, oEvent.getNote());
			longClicked(iId);
		}
		else if(eventType.equals(VirtualUIEvent.EVENT_SWIPE_RIGHT)) {
			swipeRight(iId, oEvent.getNote());
			swipeRight(iId);
		}
		else if(eventType.equals(VirtualUIEvent.EVENT_SWIPE_LEFT)) {
			swipeLeft(iId, oEvent.getNote());
			swipeLeft(iId);
		}
		else if(eventType.equals(VirtualUIEvent.EVENT_SWIPE_TOP)) {
			swipeTop(iId, oEvent.getNote());
			swipeTop(iId);
		}
		else if(eventType.equals(VirtualUIEvent.EVENT_SWIPE_BOTTOM)) {
			swipeBottom(iId, oEvent.getNote());
			swipeBottom(iId);
		}
		else if(eventType.equals(VirtualUIEvent.EVENT_VALUE_CHANGED)) {
			valueChanged(iId, oEvent.getNote());
			valueChanged(iId);
		}
		else if(eventType.equals(VirtualUIEvent.EVENT_TIMER)) {
			// For compatible
			timer(0, this.getIDForPosting().hashCode(), oEvent.getNote());
			timer(iId);
		}
		else if(eventType.equals(VirtualUIEvent.EVENT_IDLE)) {
			idle(iId, oEvent.getNote());
			idle(iId);
		}
		else if(eventType.equals(VirtualUIEvent.EVENT_FORWARD)) {
			forward(iId, "");
		}
	}
	
	public void clicked(int iId) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.clicked(iId);
	}
	
	public void longClicked(int iId) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.longClicked(iId);
	}
	
	public void swipeRight(int iId) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.swipeRight(iId);
	}
	
	public void swipeLeft(int iId) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.swipeLeft(iId);
	}
	
	public void swipeTop(int iId) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.swipeTop(iId);
	}
	
	public void swipeBottom(int iId) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.swipeBottom(iId);
	}
	
	public void valueChanged(int iId) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.valueChanged(iId);
	}
	
	public void timer(int iId) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.timer(iId);
	}
	
	public void idle(int iId) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.idle(iId);
	}
	
	public void forward(int iId, String sStatus) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.forward(iId, sStatus);
	}
	
	//	For compatible (note is not necessary)
	public boolean clicked(int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.clicked(iId, sNote);
		return false;
	}
	
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.timer(iClientSockId, iId, sNote);
		return false;
	}

	public boolean longClicked(int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.longClicked(iId, sNote);
		return false;
	}

	public boolean swipeTop(int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.swipeTop(iId, sNote);
		return false;
	}

	public boolean swipeBottom(int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.swipeBottom(iId, sNote);
		return false;
	}
	
	public boolean swipeRight(int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.swipeRight(iId, sNote);
		return false;
	}

	public boolean swipeLeft(int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.swipeLeft(iId, sNote);
		return false;
	}
	
	public boolean valueChanged(int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.valueChanged(iId, sNote);
		return false;
	}
	
	public boolean idle(int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.idle(iId, sNote);
		return false;
	}
	
	public boolean forward(int iId, String sNote, String sStatus) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.forward(iId, sNote);
		return false;
	}
	
	public boolean keyboard(int iId, String sNote) {
		if (m_oParentElementEdited != null)
			m_oParentElementEdited.keyboard(iId, sNote);
		return false;
	}
	
	protected Controller getController() {
		VirtualUIBasicElement oElement = this;
		while (oElement.m_oController == null) {
			oElement = oElement.m_oParentElementEdited;
			if (oElement == null)
				return null;
		}
		return oElement.m_oController;
	}
	
	protected Controller getParentForm() {	//	##################### to be remove
		return getController();
	}
	
	public VirtualUIBasicElement getParent(){
		return m_oParentElementEdited;
	}
	
	public ArrayList<VirtualUIBasicElement> getChilds() {
		return m_oChildElementList;
	}
	
	public int getChildCount() {
		return m_oChildElementList.size();
	}
	
	public boolean isChildId(int iElementId) {
		for(VirtualUIBasicElement oBasicElement : m_oChildElementList){
			if(oBasicElement.getId() == iElementId){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<VirtualUIBasicElement> getAllChildSet() {
		ArrayList<VirtualUIBasicElement> allChildSet = new ArrayList<VirtualUIBasicElement>();
		for (VirtualUIBasicElement oElement : m_oChildElementList) {
			allChildSet.add(oElement);
			allChildSet.addAll(oElement.getAllChildSet());
		}
		return allChildSet;
	}
	
//	public void setUIType(String type){	//	no edited version. best to define in constructor
//		if (m_sType.contentEquals(type))
//			return;
//		
//		m_sType = type;
//		
//		// Set the element to be edited
//		Core.g_oUiManager.get().setElementEdited(this);
//	}
	
	public String getUIType(){
		return m_sType;
	}
	
	public int getId(){
		return m_iId;
	}
	
	public String getIDForPosting(){
//		return (m_oParentForm.getClass().getSimpleName() + "_" + m_iId);
		return "" + m_iId;
	}
	
	public int getTop(){
		return m_iTopEdited;
	}
	
	public boolean setTop(int iTop){
		if (m_iTopEdited == iTop)
			return false;
		
		m_iTopEdited = iTop;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getLeft(){
		return m_iLeftEdited;
	}
	
	public boolean setLeft(int iLeft){
		if (m_iLeftEdited == iLeft)
			return false;
		
		m_iLeftEdited = iLeft;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getWidth(){
		return m_iWidthEdited;
	}
	
	public boolean setWidth(int iWidth){
		if (m_iWidthEdited == iWidth)
			return false;
		
		m_iWidthEdited = iWidth;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getHeight(){
		return m_iHeightEdited;
	}
	
	public boolean setHeight(int iHeight){
		if (m_iHeightEdited == iHeight)
			return false;
		
		m_iHeightEdited = iHeight;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int[] getPadding(){
		return m_iPaddingEdited;
	}
	
	public boolean setPaddingValue(String sPaddingValue) {	//	###################### to be remove
		//	Set default padding	################################### textbox padding = 3 handle later
		if (sPaddingValue.isEmpty())
			return setPadding(DEFAULT_PADDING);
		
		String[] sPaddings = sPaddingValue.split(",");
		try {
			if (sPaddings.length == 1)
				return setPadding(Integer.parseInt(sPaddings[0].trim()));
			
			int[] iPaddings = new int[4];
			
			iPaddings[0] = Integer.parseInt(sPaddings[0].trim());
			for (int i = 1; i < 4; i++) {
				if (sPaddings.length > i)
					iPaddings[i] = Integer.parseInt(sPaddings[i]);
				else
					iPaddings[i] = iPaddings[i - 1];
			}
			return setPadding(iPaddings);
		}
		catch (NumberFormatException e) {
			LoggingManager.stack2Log(e);
			return false;
		}
	}
	
	public boolean setPadding(int iPadding) {
		return setPadding(new int[] {iPadding, iPadding, iPadding, iPadding});
	}
	
	public boolean setPadding(int iPaddingTop, int iPaddingRight, int iPaddingBottom, int iPaddingLeft) {
		return setPadding(new int[] {iPaddingTop, iPaddingRight, iPaddingBottom, iPaddingLeft});
	}
	
	protected boolean setPadding(int[] iPaddings){
		if (iPaddings.length != 4)
			return false;
		
		if (m_iPaddingEdited[0] == iPaddings[0]
				&& m_iPaddingEdited[1] == iPaddings[1]
				&& m_iPaddingEdited[2] == iPaddings[2]
				&& m_iPaddingEdited[3] == iPaddings[3])
			return false;
		
		m_iPaddingEdited[0] = iPaddings[0];
		m_iPaddingEdited[1] = iPaddings[1];
		m_iPaddingEdited[2] = iPaddings[2];
		m_iPaddingEdited[3] = iPaddings[3];
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getPaddingTop() {
		return m_iPaddingEdited[0];
	}
	
	public boolean setPaddingTop(int iPaddingTop) {
		if (m_iPaddingEdited[0] == iPaddingTop)
			return false;
		
		m_iPaddingEdited[0] = iPaddingTop;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getPaddingRight() {
		return m_iPaddingEdited[1];
	}
	
	public boolean setPaddingRight(int iPaddingRight) {
		if (m_iPaddingEdited[1] == iPaddingRight)
			return false;
		
		m_iPaddingEdited[1] = iPaddingRight;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getPaddingBottom() {
		return m_iPaddingEdited[2];
	}
	
	public boolean setPaddingBottom(int iPaddingBottom) {
		if (m_iPaddingEdited[2] == iPaddingBottom)
			return false;
		
		m_iPaddingEdited[2] = iPaddingBottom;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getPaddingLeft() {
		return m_iPaddingEdited[3];
	}
	
	public boolean setPaddingLeft(int iPaddingLeft) {
		if (m_iPaddingEdited[3] == iPaddingLeft)
			return false;
		
		m_iPaddingEdited[3] = iPaddingLeft;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getStrokeWidth() {
		return m_iStrokeEdited;
	}
	
	public boolean setStroke(int iStroke) {	//	###################### to be removed
		return setStrokeWidth(iStroke);
	}
	
	public boolean setStrokeWidth(int iStroke) {
		if (m_iStrokeEdited == iStroke)
			return false;
		
		m_iStrokeEdited = iStroke;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getStrokeColor() {
		return m_sStrokeColorEdited;
	}
	
	public boolean setStrokeColor(String sStrokeColor) {	//	#### color read from platform dont have #, to be fix
		if (sStrokeColor != null && !sStrokeColor.isEmpty() && !sStrokeColor.startsWith("#"))
			sStrokeColor = "#" + sStrokeColor;
		if (m_sStrokeColorEdited.contentEquals(sStrokeColor))
			return false;
		
		m_sStrokeColorEdited = sStrokeColor;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getBackgroundColor(){
		return m_sBackgroundColorEdited;
	}
	
	public boolean setBackgroundColor(String sBackgroundColor){
		if (sBackgroundColor != null && !sBackgroundColor.isEmpty() && !sBackgroundColor.startsWith("#"))
			sBackgroundColor = "#" + sBackgroundColor;
		if (m_sBackgroundColorEdited.contentEquals(sBackgroundColor))
			return false;
		
		m_sBackgroundColorEdited = sBackgroundColor;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getForegroundColor(){
		return m_sForegroundColorEdited;
	}
	
	public boolean setForegroundColor(String sForegroundColor){
		if (sForegroundColor != null && !sForegroundColor.isEmpty() && !sForegroundColor.startsWith("#"))
			sForegroundColor = "#" + sForegroundColor;
		if (m_sForegroundColorEdited.contentEquals(sForegroundColor))
			return false;
		
		m_sForegroundColorEdited = sForegroundColor;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getValue(){
		return m_sValueEdited;
	}
	
	public boolean setValue(int iId, String sValue) {	//	###############################
		if (iId != m_iId)
			return false;
		
		return setValue(sValue);
	}
	
	public boolean setValue(String sValue){
		if (m_sValueEdited.contentEquals(sValue))
			return false;
		
		m_sValueEdited = sValue;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public boolean getGradient() {
		return m_bGradientEdited;
	}
	
	public boolean setGradient(boolean bGradient) {
		if (m_bGradientEdited == bGradient)
			return false;
		
		m_bGradientEdited = bGradient;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public boolean getVisible() {
		return m_bVisibleEdited;
	}
	
	public boolean setVisible(boolean bVisible) {
		if (m_bVisibleEdited == bVisible)
			return false;
		
		m_bVisibleEdited = bVisible;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getTextSize() {
		return m_iTextSize;
	}
	
	public boolean setTextSize(int iTextSize) {
		if (m_iTextSizeEdited == iTextSize)
			return false;
		
		if (iTextSize == 0)
			m_iTextSizeEdited = DEFAULT_TEXT_SIZE;
		else
			m_iTextSizeEdited = iTextSize;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getTextAlign(){
		return m_sTextAlignEdited;
	}
	
	public boolean setTextAlign(String sTextAlign){
		if (m_sTextAlignEdited.contentEquals(sTextAlign))
			return false;
		
		m_sTextAlignEdited = sTextAlign;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getTextStyle(){
		return m_sTextStyleEdited;
	}
	
	public boolean setTextStyle(String sTextStyle){
		if (m_sTextStyleEdited.contentEquals(sTextStyle))
			return false;
		
		m_sTextStyleEdited = sTextStyle;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getViewSeq() {
		return m_iViewSeqEdited;
	}
	
	public boolean setViewSeq(int iViewSeq) {
		if (m_iViewSeqEdited == iViewSeq)
			return false;
		
		m_iViewSeqEdited = iViewSeq;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public boolean setCornerRadius(String sCornerRadiusValue) {	//	###################### to be remove
		if (sCornerRadiusValue.isEmpty())
			return setCornerRadius(DEFAULT_CORNER_RADII);
		
		String[] sCornerRadii = sCornerRadiusValue.split(",");
		try {
			if (sCornerRadii.length == 1)
				return setCornerRadius(Integer.parseInt(sCornerRadii[0].trim()));
			
			int[] iCornerRadii = new int[4];
			
			iCornerRadii[0] = Integer.parseInt(sCornerRadii[0].trim());
			for (int i = 1; i < 4; i++) {
				if (iCornerRadii.length > i)
					iCornerRadii[i] = Integer.parseInt(sCornerRadii[i]);
				else
					iCornerRadii[i] = iCornerRadii[i - 1];
			}
			return setCornerRadius(iCornerRadii);
		}
		catch (NumberFormatException e) {
			LoggingManager.stack2Log(e);
			return false;
		}
	}
	
	public boolean setCornerRadius(int iCornerRadius) {
		boolean bUpdated = false;
		for (int i = 0; i < m_iCornerRadiiEdited.length; i++) {
			if (m_iCornerRadiiEdited[i] != iCornerRadius) {
				bUpdated = true;
				break;
			}
		}
		
		if (!bUpdated)
			return false;
		
		for (int i = 0; i < m_iCornerRadiiEdited.length; i++)
			m_iCornerRadiiEdited[i] = iCornerRadius;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	protected boolean setCornerRadius(int[] iCornerRadii){
		if (iCornerRadii.length != 4)
			return false;
		
		if (m_iCornerRadiiEdited[0] == iCornerRadii[0]
				&& m_iCornerRadiiEdited[1] == iCornerRadii[1]
				&& m_iCornerRadiiEdited[2] == iCornerRadii[2]
				&& m_iCornerRadiiEdited[3] == iCornerRadii[3])
			return false;
		
		m_iCornerRadiiEdited[0] = iCornerRadii[0];
		m_iCornerRadiiEdited[1] = iCornerRadii[1];
		m_iCornerRadiiEdited[2] = iCornerRadii[2];
		m_iCornerRadiiEdited[3] = iCornerRadii[3];
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public boolean setCornerRadius(int iCornerRadiusTop, int iCornerRadiusRight, int iCornerRadiusBottom, int iCornerRadiusLeft) {
		if (m_iCornerRadiiEdited[0] == iCornerRadiusTop
				&&	m_iCornerRadiiEdited[1] == iCornerRadiusRight
				&&	m_iCornerRadiiEdited[2] == iCornerRadiusBottom
				&&	m_iCornerRadiiEdited[3] == iCornerRadiusLeft)
			return false;
		
		m_iCornerRadiiEdited[0] = iCornerRadiusTop;
		m_iCornerRadiiEdited[1] = iCornerRadiusRight;
		m_iCornerRadiiEdited[2] = iCornerRadiusBottom;
		m_iCornerRadiiEdited[3] = iCornerRadiusLeft;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getCornerRadiusTop() {
		return m_iCornerRadiiEdited[0];
	}
	
	public boolean setCornerRadiusTop(int iCornerRadiusTop) {
		if (m_iCornerRadiiEdited[0] == iCornerRadiusTop)
			return false;
		
		m_iCornerRadiiEdited[0] = iCornerRadiusTop;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getCornerRadiusRight() {
		return m_iCornerRadiiEdited[1];
	}
	
	public boolean setCornerRadiusRight(int iCornerRadiusRight) {
		if (m_iCornerRadiiEdited[1] == iCornerRadiusRight)
			return false;
		
		m_iCornerRadiiEdited[1] = iCornerRadiusRight;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getCornerRadiusBottom() {
		return m_iCornerRadiiEdited[2];
	}
	
	public boolean setCornerRadiusBottom(int iCornerRadiusBottom) {
		if (m_iCornerRadiiEdited[2] == iCornerRadiusBottom)
			return false;
		
		m_iCornerRadiiEdited[2] = iCornerRadiusBottom;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getCornerRadiusLeft() {
		return m_iCornerRadiiEdited[3];
	}
	
	public boolean setCornerRadiusLeft(int iCornerRadiusLeft) {
		if (m_iCornerRadiiEdited[3] == iCornerRadiusLeft)
			return false;
		
		m_iCornerRadiiEdited[3] = iCornerRadiusLeft;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getSource() {
		return m_sSourceEdited;
	}
	
	public boolean setSource(String sSource) {
		if (m_sSourceEdited.contentEquals(sSource))
			return false;
		
		m_sSourceEdited = sSource;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getContentMode() {
		return m_sContentModeEdited;
	}
	
	public boolean setContentMode(String sContentMode) {
		if (m_sContentModeEdited.contentEquals(sContentMode))
			return false;
		
		m_sContentModeEdited = sContentMode;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getHint() {
		return m_sHintEdited;
	}
	
	public boolean setHint(String sHint) {
		if (m_sHintEdited.contentEquals(sHint))
			return false;
		
		m_sHintEdited = sHint;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getInputType() {
		return m_sInputTypeEdited;
	}
	
	public boolean setInputType(String sInputType) {
		if (m_sInputTypeEdited.contentEquals(sInputType))
			return false;
		
		m_sInputTypeEdited = sInputType;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public String getKeyboardType() {
		return m_sKeyboardTypeEdited;
	}
	
	public boolean setKeyboardType(String sKeyboardType) {
		if (m_sKeyboardTypeEdited.contentEquals(sKeyboardType))
			return false;
		
		m_sKeyboardTypeEdited = sKeyboardType;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getContentWidth() {
		return m_iContentWidthEdited;
	}
	
	public boolean setContentWidth(int iContentWidth) {
		if (m_iContentWidthEdited == iContentWidth)
			return false;
		
		m_iContentWidthEdited = iContentWidth;
			
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public int getContentHeight() {
		return m_iContentHeightEdited;
	}
	
	public boolean setContentHeight(int iContentHeight) {
		if (m_iContentHeightEdited == iContentHeight)
			return false;
		
		m_iContentHeightEdited = iContentHeight;
			
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public boolean setEnabled(boolean bEnabled) {
		if (m_bEnabledEdited == bEnabled)
			return false;
		
		m_bEnabledEdited = bEnabled;
		
		// Set the element to be edited
		setEdited();
		
		return true;
	}
	
	public boolean getEnabled() {
		return m_bEnabledEdited;
	}
	
//	public boolean isAllowClick(){
//		return m_oClickEventsEdited.containsKey(VirtualUIEvent.EVENT_ID_BASIC);
//	}
	
//	public boolean allowClick(boolean bHaveClickEvent){
//		if (bHaveClickEvent) {
//			if (!m_oClickEventsEdited.containsKey(VirtualUIEvent.EVENT_ID_BASIC)) {
//				//	Add new event
//				VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_ID_BASIC, m_iId);
//				m_oClickEventsEdited.put(oEvent.getId(), oEvent);
//			}
//			else
//				return false;
//		}
//		else {
//			if (m_oClickEventsEdited.remove(VirtualUIEvent.EVENT_ID_BASIC) == null)
//				return false;
//		}
//		
//		// Set the element to be edited
//		setEdited();
//		
//		return true;
//	}
	
	public VirtualUIEvent getEvent(int iEventId) {
		return m_oEventsEdited.get(iEventId);
	}
	
	public VirtualUIEvent getEventAtIndex(int iIndex) {
		return m_oEventsEdited.valueAt(iIndex);
	}
	
	public int addEvent(VirtualUIEvent oEvent) {
		oEvent.assignElement(m_iId);
		m_oEventsEdited.append(oEvent.getId(), oEvent);
		
		// Set the element to be edited
		setEdited();
		
		return oEvent.getId();
	}
	
	public void removeEvent(int iEventId) {
		m_oEventsEdited.remove(iEventId);
		
		// Set the element to be edited
		setEdited();
	}
	
	public void removeAllEvents() {
		m_oEventsEdited.clear();
		
		// Set the element to be edited
		setEdited();
	}
	
	//Sunny
	// For compatible
	public void setExist(boolean bExist) {
		m_bExist = bExist;
		
		show();
	}
	
	public boolean getExist() {
		return m_bExist;
	}
	
	public void allowClick(boolean bHaveClickEvent){
		if (bHaveClickEvent) {
			VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_CLICK);
			addEvent(oEvent);
		} else {
			for (int i = 0; i < m_oEventsEdited.size(); i++) {
				VirtualUIEvent oEvent = m_oEventsEdited.valueAt(i);
				if (oEvent.getType().equals(VirtualUIEvent.EVENT_CLICK)){
					removeEvent(oEvent.getId());
					break;
				}
			}
		}
		
		// Set the element to be edited
		setEdited();
	}
	
	public void allowLongClick(boolean bHaveLongClickEvent){
		if (bHaveLongClickEvent) {
			VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_LONG_CLICK);
			addEvent(oEvent);
		} else {
			for (int i = 0; i < m_oEventsEdited.size(); i++) {
				VirtualUIEvent oEvent = m_oEventsEdited.valueAt(i);
				if (oEvent.getType().equals(VirtualUIEvent.EVENT_LONG_CLICK)){
					removeEvent(oEvent.getId());
					break;
				}
			}
		}
		
		// Set the element to be edited
		setEdited();
	}
	
	public void allowSwipeTop(boolean bHaveSwipeTop) {
		if (bHaveSwipeTop) {
			VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_SWIPE_TOP);
			addEvent(oEvent);
		} else {
			for (int i = 0; i < m_oEventsEdited.size(); i++) {
				VirtualUIEvent oEvent = m_oEventsEdited.valueAt(i);
				if (oEvent.getType().equals(VirtualUIEvent.EVENT_SWIPE_TOP)){
					removeEvent(oEvent.getId());
					break;
				}
			}
		}
		
		// Set the element to be edited
		setEdited();
	}
	
	public void allowSwipeBottom(boolean bHaveSwipeBottom) {
		if (bHaveSwipeBottom) {
			VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_SWIPE_BOTTOM);
			addEvent(oEvent);
		} else {
			for (int i = 0; i < m_oEventsEdited.size(); i++) {
				VirtualUIEvent oEvent = m_oEventsEdited.valueAt(i);
				if (oEvent.getType().equals(VirtualUIEvent.EVENT_LONG_CLICK)){
					removeEvent(oEvent.getId());
					break;
				}
			}
		}
		
		// Set the element to be edited
		setEdited();
	}
	
	public void allowSwipeLeft(boolean bHaveSwipeLeft) {
		if (bHaveSwipeLeft) {
			VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_SWIPE_LEFT);
			addEvent(oEvent);
		} else {
			for (int i = 0; i < m_oEventsEdited.size(); i++) {
				VirtualUIEvent oEvent = m_oEventsEdited.valueAt(i);
				if (oEvent.getType().equals(VirtualUIEvent.EVENT_SWIPE_LEFT)){
					removeEvent(oEvent.getId());
					break;
				}
			}
		}
		
		// Set the element to be edited
		setEdited();
	}
	
	public void allowSwipeRight(boolean bHaveSwipeRight) {
		if (bHaveSwipeRight) {
			VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_SWIPE_RIGHT);
			addEvent(oEvent);
		} else {
			for (int i = 0; i < m_oEventsEdited.size(); i++) {
				VirtualUIEvent oEvent = m_oEventsEdited.valueAt(i);
				if (oEvent.getType().equals(VirtualUIEvent.EVENT_SWIPE_RIGHT)){
					removeEvent(oEvent.getId());
					break;
				}
			}
		}
		
		// Set the element to be edited
		setEdited();
	}
	
	public void allowValueChanged(boolean bHaveValueChanged){
		if (bHaveValueChanged) {
			VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_VALUE_CHANGED);
			addEvent(oEvent);
		}
		
		// Set the element to be edited
		setEdited();
	}
	
	public void allowForward(boolean bHaveForward) {
		if (bHaveForward) {
			VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_FORWARD);
			addEvent(oEvent);
		}
		
		// Set the element to be edited
		setEdited();
	}
	
	public void addTimer(String sId, int iInterval, boolean bRepeat, String sNote, boolean bBlockUI, boolean bAsync, VirtualUIBasicElement oElement){
		VirtualUIEvent oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_TIMER);
		// TODO
		//oEvent.setInterval(iInterval);
		oEvent.setRepeat(bRepeat);
		// In this version, use ID as Note
		oEvent.setNote(sId);
		oEvent.setBlockUI(bBlockUI);
		if (oElement != null)
			oEvent.addSubmitId(oElement.getId());
		oEvent.setAsync(bAsync);
		oEvent.setEnable(false);
		addEvent(oEvent);
	}
	
	public void controlTimer(String sId, boolean bStart) {
		for (int i = 0; i < m_oEventsEdited.size(); i++) {
			VirtualUIEvent oEvent = m_oEventsEdited.valueAt(i);
			if (oEvent.getNote().equals(sId)) {
				oEvent.setEnable(bStart);
				
				// Set the element to be edited
				setEdited();
			}
		}
	}
	
	// In version 1, only 1 event for each types is allowed. So, locate the event by type
	private VirtualUIEvent getEventByType(String sType) {
		for (int i = 0; i < m_oEventsEdited.size(); i++) {
			VirtualUIEvent oEvent = m_oEventsEdited.valueAt(i);
			if (oEvent.getType().equals(sType))
				return oEvent;
		}
		
		return null;
	}
	
	public void setClickServerRequestNote(String sNote) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_CLICK);
		if (oEvent != null) {
			oEvent.setNote(sNote);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setLongClickServerRequestNote(String sNote) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_LONG_CLICK);
		if (oEvent != null) {
			oEvent.setNote(sNote);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setSwipeLeftServerRequestNote(String sNote) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_SWIPE_LEFT);
		if (oEvent != null) {
			oEvent.setNote(sNote);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setSwipeRightServerRequestNote(String sNote) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_SWIPE_RIGHT);
		if (oEvent != null) {
			oEvent.setNote(sNote);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setClickServerRequestTimeout(int iTimeout) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_CLICK);
		if (oEvent != null) {
			// TODO
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setForwardForwardRequestTimeout(int iTimeout) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_FORWARD);
		if (oEvent != null) {
			// TODO
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setForwardForwardRequestValue(String sValue) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_FORWARD);
		if (oEvent != null) {
			// TODO
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setForwardForwardRequestDelay(int iDelay) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_FORWARD);
		if (oEvent != null) {
			// TODO
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void addClickServerRequestSubmitElement(VirtualUIBasicElement oElement) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_CLICK);
		if (oEvent != null) {
			oEvent.addSubmitId(oElement.getId());
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void addLongClickServerRequestSubmitElement(VirtualUIBasicElement oElement) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_LONG_CLICK);
		if (oEvent != null) {
			oEvent.addSubmitId(oElement.getId());
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void addValueChangedServerRequestSubmitElement(VirtualUIBasicElement oElement) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_VALUE_CHANGED);
		if (oEvent != null) {
			oEvent.addSubmitId(oElement.getId());
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void addForwardServerRequestSubmitElement(VirtualUIBasicElement oElement) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_FORWARD);
		if (oEvent != null) {
			oEvent.addSubmitId(oElement.getId());
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void addKeyboardServerRequestSubmitElement(VirtualUIBasicElement oElement) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_KEYBOARD);
		if (oEvent != null) {
			oEvent.addSubmitId(oElement.getId());
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void clearClickServerRequestSubmitId() {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_CLICK);
		if (oEvent != null) {
			oEvent.clearSubmitIdList();
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void clearLongClickServerRequestSubmitId() {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_LONG_CLICK);
		if (oEvent != null) {
			oEvent.clearSubmitIdList();
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setClickServerRequestBlockUI(boolean bBlockUI) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_CLICK);
		if (oEvent != null) {
			oEvent.setBlockUI(bBlockUI);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setLongClickServerRequestBlockUI(boolean bBlockUI) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_LONG_CLICK);
		if (oEvent != null) {
			oEvent.setBlockUI(bBlockUI);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setSwipeLeftServerRequestBlockUI(boolean bBlockUI) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_SWIPE_LEFT);
		if (oEvent != null) {
			oEvent.setBlockUI(bBlockUI);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setSwipeRightServerRequestBlockUI(boolean bBlockUI) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_SWIPE_RIGHT);
		if (oEvent != null) {
			oEvent.setBlockUI(bBlockUI);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setForwardServerRequestBlockUI(boolean bBlockUI) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_FORWARD);
		if (oEvent != null) {
			oEvent.setBlockUI(bBlockUI);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setClickReplaceValue(String sReplaceValueRegex, String sReplaceValue) {	//	###### temp function handle default elementId (focus)
		setClickReplaceValue(HeroActionProtocol.View.Id.FOCUS, sReplaceValueRegex, sReplaceValue);
	}
	
	public void setClickReplaceValue(String sElementId, String sReplaceValueRegex, String sReplaceValue){
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_CLICK);
		if (oEvent != null) {
			VirtualUIAction oAction = new VirtualUIAction(sElementId);
			oAction.replaceValue(sReplaceValueRegex, sReplaceValue);
			oEvent.addVirtualUIAction(oAction);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setLongClickReplaceValue(String sReplaceValueRegex, String sReplaceValue) {	//	###### temp function handle default elementId (focus)
		setLongClickReplaceValue(HeroActionProtocol.View.Id.FOCUS, sReplaceValueRegex, sReplaceValue);
	}
	
	public void setLongClickReplaceValue(String sElementId, String sReplaceValueRegex, String sReplaceValue){
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_LONG_CLICK);
		if (oEvent != null) {
			VirtualUIAction oAction = new VirtualUIAction(sElementId);
			oAction.replaceValue(sReplaceValueRegex, sReplaceValue);
			oEvent.addVirtualUIAction(oAction);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setClickHideKeyboard(boolean bAction) {	//	###### temp function handle default elementId (focus)
		setClickHideKeyboard(HeroActionProtocol.View.Id.FOCUS, bAction);
	}
	
	public void setClickHideKeyboard(String sElementId, boolean bAction) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_CLICK);
		if (oEvent != null) {
			VirtualUIAction oAction = new VirtualUIAction(sElementId);
			// TODO
			//oAction.hideKeyboard();
			oEvent.addVirtualUIAction(oAction);
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setForwardForwardRequestType(String sType) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_FORWARD);
		if (oEvent != null) {
			// TODO
			//oEvent.setType();
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setForwardForwardRequestAddress(String sAddress) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_FORWARD);
		if (oEvent != null) {
			// TODO
			//oEvent.setAddress();
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public void setForwardForwardRequestPort(int iPort) {
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_FORWARD);
		if (oEvent != null) {
			// TODO
			//oEvent.setPort();
			
			// Set the element to be edited
			setEdited();
		}
	}
	
	public String getClickServerRequestNote() {
		String sNote = "";
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_CLICK);
		if (oEvent != null) {
			sNote = oEvent.getNote();
		}
		
		return sNote;
	}
	
	public String getSwipeLeftServerRequestNote() {
		String sNote = "";
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_SWIPE_LEFT);
		if (oEvent != null) {
			sNote = oEvent.getNote();
		}
		
		return sNote;
	}
	
	public String getSwipeRightServerRequestNote() {
		String sNote = "";
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_SWIPE_RIGHT);
		if (oEvent != null) {
			sNote = oEvent.getNote();
		}
		
		return sNote;
	}
	
	public String getForwardServerRequestNote() {
		String sNote = "";
		VirtualUIEvent oEvent = getEventByType(VirtualUIEvent.EVENT_FORWARD);
		if (oEvent != null) {
			sNote = oEvent.getNote();
		}
		
		return sNote;
	}
	
	public void addKeyboardKeyCode(int iKeyCode) {
		// TODO
	}
	
	public void setFocus() {
		this.focus();
	}
	
	public void bringToTop() {
		this.top();
	}
	
	public void scrollToPosition(int iPosition) {
		if(iPosition == 1)
			this.scrollToTop();
		else
		if(iPosition == 2)
			this.scrollToBottom();
	}
	
	public void setReplaceValue(String sReplaceValueRegex, String sReplaceValue) {
		this.replaceValue(sReplaceValueRegex, sReplaceValue);
	}
	
	public int getStroke() {
		return getStrokeWidth();
	}
	
	// No use
	public void supportReplaceValueWithClickEvent(boolean bSupport) {
		
	}
	
//	public boolean addTimer(String sId, int iInterval, boolean bRepeat, String sNote, boolean bBlockUI, boolean bAsync, VirtualUIBasicElement oElement){
//		VirtualUITimerEvent oTimer = new VirtualUITimerEvent(sId, m_iId);
//		oTimer.setTime(iInterval);
//		oTimer.setRepeat(bRepeat);
//		oTimer.setServerRequestNote(sNote);
//		oTimer.setServerRequestBlockUI(bBlockUI);
//		oTimer.setAsync(bAsync);
//		if (oElement != null)
//			oTimer.addSubmitId(oElement.m_iId);
//		
//		m_oTimerEventsEdited.put(oTimer.getId(), oTimer);
//		
//		// Set the element to be edited
//		setEdited();
//		
//		return true;
//	}
//	
//	public boolean controlTimer(String sId, boolean bStart){
//		VirtualUITimerEvent oTimer = (VirtualUITimerEvent)m_oTimerEventsEdited.get(sId);
//		if (oTimer == null)
//			return false;
//		
//		if (oTimer.setEnable(bStart)) {
//			// Set the element to be edited
//			setEdited();
//			
//			return true;
//		}
//		else {
//			return false;
//		}
//	}

	// Value Changed
//	public void allowValueChanged(boolean bAllow) {	//	#################################
//		return;
//	}
//	
//	public void setValueChangedServerRequestNote(String sNote){	//	#######################################################
////		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
////		oServerRequest.setServerRequestNote(sNote);
//	}
//		
//	public String getValueChangedServerRequestNote(){	//	#######################################################
////		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
////		return oServerRequest.getServerRequestNote();
//		return "";
//	}
	
//	public void addValueChangedServerRequestSubmitElement(VirtualUIBasicElement oElement){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
//		oServerRequest.addServerRequestSubmitElement(oElement);
//		
//		if(m_bShow){
//			// The UI is shown ==> Modify UI
//			// Create packet to update UI
//			JSONObject oView = new JSONObject();
//			JSONObject oValueChangedEvent = new JSONObject();
//			JSONArray oValueChangedEventArray = new JSONArray();
//			JSONObject oEvent = new JSONObject();
//			JSONObject oJSONServerRequest = new JSONObject();
//			
//			try {
//				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
//				oValueChangedEvent.put(HeroActionProtocol.View.Event.ValueChanged.Id.KEY, "1");
//				if(this.getValueChangedServerRequestSubmitId().length() > 0)
//					oJSONServerRequest.put(HeroActionProtocol.View.Event.ValueChanged.ServerRequest.SubmitId.KEY, this.getValueChangedServerRequestSubmitId());
//				oValueChangedEvent.put(HeroActionProtocol.View.Event.ValueChanged.ServerRequest.KEY, oJSONServerRequest);
//				oValueChangedEventArray.put(oValueChangedEvent);
//				oEvent.put(HeroActionProtocol.View.Event.ValueChanged.KEY, oValueChangedEventArray);
//				
//				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
//			}
//			catch (Exception e) {}
//			
//			m_oParentTerm.appendPacket(oView);
//		}
//	}
	
//	public JSONArray getValueChangedServerRequestSubmitId(){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
//		return oServerRequest.getServerRequestSubmitId();
//		return new JSONArray();
//	}
	
//	public void clearValueChangedServerRequestSubmitId(){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
//		oServerRequest.clearServerRequestSubmitId();
//	}
	
//	public void setValueChangedServerRequestBlockUI(boolean bBlockUI){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
//		oServerRequest.setServerRequestBlockUI(bBlockUI);
//	}
	
//	public boolean getValueChangedServerRequestBlockUI(){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("value_changed");
//		return oServerRequest.getServerRequestBlockUI();
//		return false;
//	}
	
//	public void allowForward(boolean bAllow) {
		//	##################
//	}
	
	// Forward
//	public void setForwardServerRequestNote(String sNote){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//		oServerRequest.setServerRequestNote(sNote);
//	}
		
//	public String getForwardServerRequestNote(){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//		return oServerRequest.getServerRequestNote();
//		return "";
//	}
	
//	public void addForwardServerRequestSubmitElement(VirtualUIBasicElement oElement){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//		oServerRequest.addServerRequestSubmitElement(oElement);
//		
//		if(m_bShow){
//			// The UI is shown ==> Modify UI
//			// Create packet to update UI
//			JSONObject oView = new JSONObject();
//			JSONObject oForwardEvent = new JSONObject();
//			JSONArray oForwardEventArray = new JSONArray();
//			JSONObject oEvent = new JSONObject();
//			JSONObject oJSONServerRequest = new JSONObject();
//			
//			try {
//				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.Id.KEY, "1");
//				if(this.getForwardServerRequestSubmitId().length() > 0)
//					oJSONServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.SubmitId.KEY, this.getForwardServerRequestSubmitId());
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ServerRequest.KEY, oJSONServerRequest);
//				oForwardEventArray.put(oForwardEvent);
//				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oForwardEventArray);
//				
//				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
//			}
//			catch (Exception e) {}
//			
//			m_oParentTerm.appendPacket(oView);
//		}
//	}
	
//	public JSONArray getForwardServerRequestSubmitId(){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//		return oServerRequest.getServerRequestSubmitId();
//		return new JSONArray();
//	}
	
//	public void clearForwardServerRequestSubmitId(){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//		oServerRequest.clearServerRequestSubmitId();
//	}
	
//	public void setForwardServerRequestBlockUI(boolean bBlockUI){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//		oServerRequest.setServerRequestBlockUI(bBlockUI);
//	}
	
//	public boolean getForwardServerRequestBlockUI(){	//	#######################################################
//		VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//		return oServerRequest.getServerRequestBlockUI();
//		return false;
//	}

//	public void setForwardForwardRequestType(String sType){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		oForwardRequest.setForwardRequestType(sType);
//	}
		
//	public String getForwardForwardRequestType(){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		return oForwardRequest.getForwardRequestType();
//		return "";
//	}
	
//	public void setForwardForwardRequestAddress(String sAddress){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		oForwardRequest.setForwardRequestAddress(sAddress);
//	}
		
//	public String getForwardForwardRequestAddress(){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		return oForwardRequest.getForwardRequestAddress();
//		return "";
//	}
	
//	public void setForwardForwardRequestPort(int iPort){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		oForwardRequest.setForwardRequestPort(iPort);
//	}
		
//	public int getForwardForwardRequestPort(){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		return oForwardRequest.getForwardRequestPort();
//		return 0;
//	}
	
//	public void setForwardForwardRequestValue(String sValue){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		oForwardRequest.setForwardRequestValue(sValue);
//		
//		if(m_bShow){
//			// Increment event version
//			oForwardRequest.incrementForwardRequestVersion();
//			VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//			oServerRequest.setServerRequestNote(oForwardRequest.getForwardRequestVersion()+"");
//			
//			// The UI is shown ==> Modify UI
//			// Create packet to update UI
//			JSONObject oView = new JSONObject();
//			JSONObject oForwardEvent = new JSONObject();
//			JSONArray oForwardEventArray = new JSONArray();
//			JSONObject oEvent = new JSONObject();
//			JSONObject oForwardRequestJSON = new JSONObject();
//			JSONObject oJSONServerRequest = new JSONObject();
//			
//			try {
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.Id.KEY, "Forward1");
//				oForwardRequestJSON.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Value.KEY, sValue);
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.KEY, oForwardRequestJSON);
//				
//				oJSONServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.Note.KEY, this.getForwardServerRequestNote());
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ServerRequest.KEY, oJSONServerRequest);
//								
//				oForwardEventArray.put(oForwardEvent);
//				oEvent.put(HeroActionProtocol.View.Event.Forward.KEY, oForwardEventArray);
//				
//				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
//				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
//			}
//			catch (Exception e) {}
//			
//			m_oParentTerm.appendPacket(oView);
//		}
//	}
		
//	public String getForwardForwardRequestValue(){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		return oForwardRequest.getForwardRequestValue();
//		return "";
//	}
	
//	public void setForwardForwardRequestTimeout(int iTimeout){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		oForwardRequest.setForwardRequestTimeout(iTimeout);
//		
//		if(m_bShow){
//			// Increment event version
//			oForwardRequest.incrementForwardRequestVersion();
//			VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//			oServerRequest.setServerRequestNote(oForwardRequest.getForwardRequestVersion()+"");
//			
//			// The UI is shown ==> Modify UI
//			// Create packet to update UI
//			JSONObject oView = new JSONObject();
//			JSONObject oForwardEvent = new JSONObject();
//			JSONArray oForwardEventArray = new JSONArray();
//			JSONObject oEvent = new JSONObject();
//			JSONObject oForwardRequestJSON = new JSONObject();
//			JSONObject oJSONServerRequest = new JSONObject();
//			
//			try {
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.Id.KEY, "Forward1");
//				oForwardRequestJSON.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Timeout.KEY, iTimeout);
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.KEY, oForwardRequestJSON);
//				
//				oJSONServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.Note.KEY, this.getForwardServerRequestNote());
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ServerRequest.KEY, oJSONServerRequest);
//				
//				oForwardEventArray.put(oForwardEvent);
//				oEvent.put(HeroActionProtocol.View.Event.Forward.KEY, oForwardEventArray);
//				
//				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
//				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
//			}
//			catch (Exception e) {}
//			
//			m_oParentTerm.appendPacket(oView);
//		}
//	}
		
//	public int getForwardForwardRequestTimeout(){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		return oForwardRequest.getForwardRequestTimeout();
//		return 0;
//	}
	
//	public void setForwardForwardRequestDelay(int iDelay){
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		oForwardRequest.setForwardRequestDelay(iDelay);
//		
//		if(m_bShow){
//			// Increment event version
//			oForwardRequest.incrementForwardRequestVersion();
//			VirtualUIServerRequest oServerRequest = m_oEventServerRequest.get("forward");
//			oServerRequest.setServerRequestNote(oForwardRequest.getForwardRequestVersion()+"");
//			
//			// The UI is shown ==> Modify UI
//			// Create packet to update UI
//			JSONObject oView = new JSONObject();
//			JSONObject oForwardEvent = new JSONObject();
//			JSONArray oForwardEventArray = new JSONArray();
//			JSONObject oEvent = new JSONObject();
//			JSONObject oForwardRequestJSON = new JSONObject();
//			JSONObject oJSONServerRequest = new JSONObject();
//			
//			try {
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.Id.KEY, "Forward1");
//				oForwardRequestJSON.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Delay.KEY, iDelay);
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.KEY, oForwardRequestJSON);
//				
//				oJSONServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.Note.KEY, this.getForwardServerRequestNote());
//				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ServerRequest.KEY, oJSONServerRequest);
//				
//				oForwardEventArray.put(oForwardEvent);
//				oEvent.put(HeroActionProtocol.View.Event.Forward.KEY, oForwardEventArray);
//				
//				oView.put(HeroActionProtocol.View.Id.KEY, getIDForPosting());
//				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
//			}
//			catch (Exception e) {}
//			
//			m_oParentTerm.appendPacket(oView);
//		}
//	}
		
//	public int getForwardForwardRequestDelay(){
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		return oForwardRequest.getForwardRequestDelay();
//		return 0;
//	}
	
//	public void setForwardForwardRequestBlockUI(boolean bBlockUI){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		oForwardRequest.setForwardRequestBlockUI(bBlockUI);
//	}
	
//	public boolean getForwardForwardRequestBlockUI(){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		return oForwardRequest.getForwardRequestBlockUI();
//		return false;
//	}
	
//	public long getForwardForwardRequestVersion(){	//	#######################################################
//		VirtualUIForwardRequest oForwardRequest = m_oEventForwardRequest.get("forward");
//		return oForwardRequest.getForwardRequestVersion();
//		return 0;
//	}
	
//	public boolean replaceValue(String sRegex, String sValue) {
//		if (sRegex == null || sRegex.isEmpty()) {
//			m_oDoReplaceValue = null;
//			return false;
//		}
//		
//		if (m_oDoReplaceValue == null)
//			m_oDoReplaceValue = new VirtualUIDoReplaceValue();
//		
//		m_oDoReplaceValue.setRegex(sRegex);
//		m_oDoReplaceValue.setValue(sValue);
//		
//		// Set the element to be edited
//		setEdited();
//		
//		return true;
//	}
//	
//	public boolean setReplaceValue(String sElementId, String sRegex, String sValue) {
//		if (sElementId == null)
//			sElementId = HeroActionProtocol.View.Id.FOCUS;
//		
//		VirtualUIEvent oEvent = m_oClickEvents.get(VirtualUIEvent.EVENT_ID_REPLACE_VALUE);
//		if (oEvent == null)
//			oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_ID_REPLACE_VALUE, m_iId);
//		
//		VirtualUIAction oAction = new VirtualUIAction(sElementId);
//		oAction.replaceValue(sRegex, sValue);
//		oEvent.addVirtualUIAction(oAction);
//		m_oClickEventsEdited.put(oEvent.getId(), oEvent);
//		
//		// Set the element to be edited
//		setEdited();
//		
//		return true;
//	}
//	
//	public boolean addClickReplaceValue(String sElementId, String sRegex, String sValue) {
//		if (sElementId == null)
//			sElementId = HeroActionProtocol.View.Id.FOCUS;
//		
//		//	New event
//		VirtualUIEvent oEvent = new VirtualUIEvent(null, m_iId);
//		VirtualUIAction oAction = new VirtualUIAction(sElementId);
//		oAction.replaceValue(sRegex, sValue);
//		oEvent.addVirtualUIAction(oAction);
//		m_oClickEventsEdited.put(oEvent.getId(), oEvent);
//		
//		// Set the element to be edited
//		setEdited();
//		
//		return true;
//	}
//	
//	public boolean setClickHideKeyboard() {
//		VirtualUIEvent oEvent = m_oClickEvents.get(VirtualUIEvent.EVENT_ID_HIDE_KEYBOARD);
//		if (oEvent != null)
//			return false;
//		
//		//	New event
//		oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_ID_HIDE_KEYBOARD, m_iId);
//		VirtualSystemAction oSystemAction = oEvent.getVirtualSystemAction();
//		oSystemAction.setHideKeyboard(true);
//		oSystemAction.setShowKeyboard(false);
//		m_oClickEventsEdited.put(oEvent.getId(), oEvent);
//		
//		// Set the element to be edited
//		setEdited();
//		
//		return true;
//	}
//	
//	public boolean setClickShowKeyboard() {
//		VirtualUIEvent oEvent = m_oClickEvents.get(VirtualUIEvent.EVENT_ID_TRIGGER_KEYBOARD);
//		if (oEvent != null)
//			return false;
//		
//		//	New event
//		oEvent = new VirtualUIEvent(VirtualUIEvent.EVENT_ID_TRIGGER_KEYBOARD, m_iId);
//		VirtualSystemAction oSystemAction = oEvent.getVirtualSystemAction();
//		oSystemAction.setHideKeyboard(false);
//		oSystemAction.setShowKeyboard(true);
//		m_oClickEvents.put(oEvent.getId(), oEvent);
//		
//		// Set the element to be edited
//		setEdited();
//		
//		return true;
//	}
}
