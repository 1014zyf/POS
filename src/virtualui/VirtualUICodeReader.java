package virtualui;

import org.json.JSONArray;
import org.json.JSONObject;

public class VirtualUICodeReader extends VirtualUIFrame {
	
	// Constructor
	public VirtualUICodeReader(){
		super.setUIType(HeroActionProtocol.View.Type.CODE_READER);
	}
	
	@Override
	public void show(){
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oAttribute = new JSONObject();
		VirtualUIForm parentForm = this.getParentForm();
		VirtualUIBasicElement parentElement = this.getParent();
		JSONObject oEvent = new JSONObject();
		JSONObject oValueChangedEvent = new JSONObject();
		JSONArray oValueChangedEventArray = new JSONArray();
		
		// Check if parent is shown before
		if(!parentForm.isShow())
			return;
		
		try {
			//oAttribute.put("Class", this.getClass().getSimpleName());
			oAttribute.put(HeroActionProtocol.View.Attribute.Width.KEY, super.getWidth());
			oAttribute.put(HeroActionProtocol.View.Attribute.Height.KEY, super.getHeight());
			oAttribute.put(HeroActionProtocol.View.Attribute.Top.KEY, super.getTop());
			oAttribute.put(HeroActionProtocol.View.Attribute.Left.KEY, super.getLeft());
			oAttribute.put(HeroActionProtocol.View.Attribute.Padding.KEY, super.getPaddingValue());
			oAttribute.put(HeroActionProtocol.View.Attribute.Background.KEY, super.getBackgroundColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Stroke.KEY, super.getStroke());
			oAttribute.put(HeroActionProtocol.View.Attribute.StrokeColor.KEY, super.getStrokeColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Gradient.KEY, super.isGradient());
			oAttribute.put(HeroActionProtocol.View.Attribute.CornerRadius.KEY, super.getCornerRadius());
			oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, super.getVisible());
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
			oAttribute.put(HeroActionProtocol.View.Attribute.ViewSeq.KEY, super.getViewSeq()-1);
			if (super.getShadowRadius() > 0) {
				JSONObject oShadow = new JSONObject();
				oShadow.put(HeroActionProtocol.View.Attribute.Shadow.Top.KEY, super.getShadowTop());
				oShadow.put(HeroActionProtocol.View.Attribute.Shadow.Left.KEY, super.getShadowLeft());
				oShadow.put(HeroActionProtocol.View.Attribute.Shadow.Radius.KEY, super.getShadowRadius());
				oShadow.put(HeroActionProtocol.View.Attribute.Shadow.Color.KEY, super.getShadowColor());
				oAttribute.put(HeroActionProtocol.View.Attribute.Shadow.KEY, oShadow);
			}
			
			if(super.isAllowValueChanged()){
				JSONObject oServerRequest = new JSONObject();
				oValueChangedEvent.put(HeroActionProtocol.View.Event.ValueChanged.Id.KEY, "CodeReader1");
				oServerRequest.put(HeroActionProtocol.View.Event.ValueChanged.ServerRequest.Note.KEY, super.getValueChangedServerRequestNote());
				if(super.getValueChangedServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.ValueChanged.ServerRequest.SubmitId.KEY, super.getValueChangedServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.ValueChanged.ServerRequest.BlockUI.KEY, super.getValueChangedServerRequestBlockUI());
				oServerRequest.put(HeroActionProtocol.View.Event.ValueChanged.ServerRequest.Timeout.KEY, super.getValueChangedServerRequestTimeout());
				oValueChangedEvent.put(HeroActionProtocol.View.Event.ValueChanged.ServerRequest.KEY, oServerRequest);
				oValueChangedEventArray.put(oValueChangedEvent);
				oEvent.put(HeroActionProtocol.View.Event.ValueChanged.KEY, oValueChangedEventArray);
			}
			
			oView.put(HeroActionProtocol.View.Id.KEY, super.getIDForPosting());
			oView.put(HeroActionProtocol.View.Type.KEY, super.getUIType());
			if (parentElement != null && parentElement.getId() != 0) {
				oView.put(HeroActionProtocol.View.ParentId.KEY, parentElement.getIDForPosting());
			}
			oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
		}
		catch (Exception e) {}
		
		super.getParentTerm().appendPacket(oView);
		
		super.show();
	}
}
