package virtualui;

import org.json.JSONArray;
import org.json.JSONObject;

public class VirtualUICanvas extends VirtualUIBasicElement {
	private String m_sKeyboardType;
	private String m_sInputType;
	private String m_sHint;
	
	// Constructor
	public VirtualUICanvas(){
		
		m_sKeyboardType = HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT;
		m_sInputType = HeroActionProtocol.View.Attribute.InputType.DEFAULT;
		m_sHint = "";
		
		super.setUIType(HeroActionProtocol.View.Type.CANVAS);
		super.setEnabled(true);
	}
	
	public String getKeyboardType(){
		return m_sKeyboardType;
	}
	
	public void setKeyboardType(String sType){
		m_sKeyboardType = sType;
	}
	
	public void setInputType(String sType){
		m_sInputType = sType;
	}
	
	public void setHint(String sHint){
		m_sHint = sHint;
	}
	
	@Override
	public void show(){
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oAttribute = new JSONObject();
		VirtualUIBasicElement parentElement = this.getParent();
		JSONObject oClickEvent = new JSONObject();
		JSONArray oClickEventArray = new JSONArray();
		JSONObject oEvent = new JSONObject();
		JSONObject oDo = new JSONObject();
		
		// Check if parent is shown before
		if(!parentElement.isShow())
			return;
		
		try {
			oAttribute.put(HeroActionProtocol.View.Attribute.Width.KEY, super.getWidth());
			oAttribute.put(HeroActionProtocol.View.Attribute.Height.KEY, super.getHeight());
			oAttribute.put(HeroActionProtocol.View.Attribute.Top.KEY, super.getTop());
			oAttribute.put(HeroActionProtocol.View.Attribute.Left.KEY, super.getLeft());
			oAttribute.put(HeroActionProtocol.View.Attribute.Padding.KEY, super.getPaddingValue());
			oAttribute.put(HeroActionProtocol.View.Attribute.Background.KEY, super.getBackgroundColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Foreground.KEY, super.getForegroundColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Stroke.KEY, super.getStroke());
			oAttribute.put(HeroActionProtocol.View.Attribute.StrokeColor.KEY, super.getStrokeColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.CornerRadius.KEY, super.getCornerRadius());
			oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, super.getVisible());
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
			oAttribute.put(HeroActionProtocol.View.Attribute.Value.KEY, super.getValue());
			oAttribute.put(HeroActionProtocol.View.Attribute.CornerRadius.KEY, super.getCornerRadius());
			if (super.getTextSize() != 0)
				oAttribute.put(HeroActionProtocol.View.Attribute.TextSize.KEY, super.getTextSize());
			if(m_sKeyboardType != HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT){
				oAttribute.put(HeroActionProtocol.View.Attribute.KeyboardType.KEY, m_sKeyboardType);
			}
			if(m_sInputType != HeroActionProtocol.View.Attribute.InputType.DEFAULT){
				oAttribute.put(HeroActionProtocol.View.Attribute.InputType.KEY, m_sInputType);
			}
			if(!m_sHint.isEmpty()){
				oAttribute.put(HeroActionProtocol.View.Attribute.Hint.KEY, m_sHint);
			}
			if(super.getClickHideKeyboard()){
				JSONObject oAction = new JSONObject();
				JSONObject oSystem = new JSONObject();
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				oSystem.put(HeroActionProtocol.System.HideKeyboard.KEY, "");
				oAction.put(HeroActionProtocol.System.KEY, oSystem);
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Action.KEY, oAction);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
			}
			if(super.getFocusWhenShow()){
				oDo.put(HeroActionProtocol.View.Do.Focus.KEY, new JSONObject());
			}
			
			oView.put(HeroActionProtocol.View.Id.KEY, super.getIDForPosting());
			oView.put(HeroActionProtocol.View.Type.KEY, super.getUIType());
			oView.put(HeroActionProtocol.View.ParentId.KEY, parentElement.getIDForPosting());
			oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
			oView.put(HeroActionProtocol.View.Do.KEY, oDo);
		}
		catch (Exception e) {}
		
		super.getParentTerm().appendPacket(oView);
		
		super.show();
	}
}
