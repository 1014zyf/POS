package virtualui;

import org.json.JSONArray;
import org.json.JSONObject;

public class VirtualUIKeyboardReader extends VirtualUIBasicElement {
	// Constructor
	public VirtualUIKeyboardReader(){
		super.setUIType(HeroActionProtocol.View.Type.KEYBOARD_READER);
		
		// Must show
		this.setExist(true);
	}
	
	@Override
	public void show(){
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oAttribute = new JSONObject();
		JSONObject oKeyboardEvent = new JSONObject();
		JSONArray oKeyboardEventArray = new JSONArray();
		JSONObject oEvent = new JSONObject();
		JSONObject oAction = new JSONObject();
		JSONObject oServerRequest = new JSONObject();
		
		VirtualUIBasicElement parentElement = this.getParent();
		
		// Check if parent is shown before
		if(!parentElement.isShow())
			return;
		
		try {
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
			
			oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.Id.KEY, "1");
			
			// key code
			if(super.getKeyboardKeyCode().length() > 0)
				oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.KeyCode.KEY, super.getKeyboardKeyCode());
						
			// Server action
			if (super.getKeyboardServerRequestSubmitId().length() > 0 || super.getKeyboardReplaceValueCount() > 0) {
				if(super.getKeyboardServerRequestSubmitId().length() > 0) {
					oServerRequest.put(HeroActionProtocol.View.Event.Keyboard.ServerRequest.SubmitId.KEY, super.getKeyboardServerRequestSubmitId());
					if(this.getKeyboardServerRequestNote().length() > 0)
						oServerRequest.put(HeroActionProtocol.View.Event.Keyboard.ServerRequest.Note.KEY, this.getKeyboardServerRequestNote());
					oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.ServerRequest.KEY, oServerRequest);
				}
				oAction.put(HeroActionProtocol.View.KEY, super.buildKeyboardActionViewArray());
				oKeyboardEvent.put(HeroActionProtocol.View.Event.Keyboard.Action.KEY, oAction);
			}
			
			oKeyboardEventArray.put(oKeyboardEvent);
			oEvent.put(HeroActionProtocol.View.Event.Keyboard.KEY, oKeyboardEventArray);
						
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
