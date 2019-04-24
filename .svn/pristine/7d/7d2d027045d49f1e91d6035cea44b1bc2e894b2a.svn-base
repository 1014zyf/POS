package virtualui;

import org.json.JSONArray;
import org.json.JSONObject;

public class VirtualUIImage extends VirtualUIBasicElement {
	private String m_sSource;
	private String m_sContentMode;
	
	// Constructor
	public VirtualUIImage(){
		super.setUIType(HeroActionProtocol.View.Type.IMAGE);
	}
	
	public String getSource(){
		return m_sSource;
	}
	
	public void setSource(String sValue){
		setExist(true);
		m_sSource = sValue;
		
		if(super.isShow()){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.Source.KEY, m_sSource);
				
				oView.put(HeroActionProtocol.View.Id.KEY, super.getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			super.getParentTerm().appendPacket(oView);
		}
	}
	
	public String getContentMode(){
		return m_sContentMode;
	}
	
	public void setContentMode(String sContentMode){
		m_sContentMode = sContentMode;
		
		if(super.isShow()){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.ContentMode.KEY, m_sContentMode);
				
				oView.put(HeroActionProtocol.View.Id.KEY, super.getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			super.getParentTerm().appendPacket(oView);
		}
	}
	
	@Override
	public void show(){
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oAttribute = new JSONObject();
		JSONObject oClickEvent = new JSONObject();
		JSONArray oClickEventArray = new JSONArray();
		JSONObject oLongClickEvent = new JSONObject();
		JSONArray oLongClickEventArray = new JSONArray();
		JSONObject oEvent = new JSONObject();
		
		VirtualUIBasicElement parentElement = this.getParent();
		
		// Check if parent is shown before
		if(!parentElement.isShow())
			return;
		
		try {
			oAttribute.put(HeroActionProtocol.View.Attribute.Width.KEY, super.getWidth());
			oAttribute.put(HeroActionProtocol.View.Attribute.Height.KEY, super.getHeight());
			oAttribute.put(HeroActionProtocol.View.Attribute.Top.KEY, super.getTop());
			oAttribute.put(HeroActionProtocol.View.Attribute.Left.KEY, super.getLeft());
			oAttribute.put(HeroActionProtocol.View.Attribute.Padding.KEY, super.getPaddingValue());
			oAttribute.put(HeroActionProtocol.View.Attribute.Source.KEY, m_sSource);
			oAttribute.put(HeroActionProtocol.View.Attribute.ContentMode.KEY, m_sContentMode);
			oAttribute.put(HeroActionProtocol.View.Attribute.Background.KEY, super.getBackgroundColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Stroke.KEY, super.getStroke());
			oAttribute.put(HeroActionProtocol.View.Attribute.StrokeColor.KEY, super.getStrokeColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, super.getVisible());
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
			oAttribute.put(HeroActionProtocol.View.Attribute.CornerRadius.KEY, super.getCornerRadius());
			if (super.getShadowRadius() > 0) {
				JSONObject oShadow = new JSONObject();
				oShadow.put(HeroActionProtocol.View.Attribute.Shadow.Top.KEY, super.getShadowTop());
				oShadow.put(HeroActionProtocol.View.Attribute.Shadow.Left.KEY, super.getShadowLeft());
				oShadow.put(HeroActionProtocol.View.Attribute.Shadow.Radius.KEY, super.getShadowRadius());
				oShadow.put(HeroActionProtocol.View.Attribute.Shadow.Color.KEY, super.getShadowColor());
				oAttribute.put(HeroActionProtocol.View.Attribute.Shadow.KEY, oShadow);
			}
			
			if(super.isAllowClick()){
				JSONObject oServerRequest = new JSONObject();
				oClickEvent.put(HeroActionProtocol.View.Event.Click.Id.KEY, "1");
				oServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.Note.KEY, super.getClickServerRequestNote());
				if(super.getClickServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.SubmitId.KEY, super.getClickServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.BlockUI.KEY, super.getClickServerRequestBlockUI());
				oServerRequest.put(HeroActionProtocol.View.Event.Click.ServerRequest.Timeout.KEY, super.getClickServerRequestTimeout());
				oClickEvent.put(HeroActionProtocol.View.Event.Click.ServerRequest.KEY, oServerRequest);
				oClickEventArray.put(oClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.Click.KEY, oClickEventArray);
			}
			
			if(super.isAllowLongClick()){
				JSONObject oServerRequest = new JSONObject();
				oLongClickEvent.put(HeroActionProtocol.View.Event.LongClick.Id.KEY, "LongClick1");
				oServerRequest.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.Note.KEY, super.getLongClickServerRequestNote());
				if(super.getLongClickServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.SubmitId.KEY, super.getLongClickServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.BlockUI.KEY, super.getLongClickServerRequestBlockUI());
				oLongClickEvent.put(HeroActionProtocol.View.Event.LongClick.ServerRequest.KEY, oServerRequest);
				oLongClickEventArray.put(oLongClickEvent);
				oEvent.put(HeroActionProtocol.View.Event.LongClick.KEY, oLongClickEventArray);
			}
			
			oView.put(HeroActionProtocol.View.Id.KEY, super.getIDForPosting());
			oView.put(HeroActionProtocol.View.Type.KEY, super.getUIType());
			oView.put(HeroActionProtocol.View.ParentId.KEY, parentElement.getIDForPosting());
			oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
		}
		catch (Exception e) {}
		
		super.getParentTerm().appendPacket(oView);
		
		super.show();
	}
}
