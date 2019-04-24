package virtualui;

import org.json.JSONArray;
import org.json.JSONObject;

public class VirtualUISwipeCardReader extends VirtualUIBasicElement {
	
	// Constructor
	public VirtualUISwipeCardReader(){
		super.setUIType(HeroActionProtocol.View.Type.SWIPE_CARD_READER);
		
		// Must show
		this.setExist(true);
	}
	
	@Override
	public void show(){
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oAttribute = new JSONObject();
		JSONObject oValueChangedEvent = new JSONObject();
		JSONArray oValueChangedEventArray = new JSONArray();
		JSONObject oEvent = new JSONObject();
		
		try {
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
						
			if(super.isAllowValueChanged()){
				JSONObject oServerRequest = new JSONObject();
				oValueChangedEvent.put(HeroActionProtocol.View.Event.ValueChanged.Id.KEY, "SwipeCardReader1");
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
			oView.put(HeroActionProtocol.View.ValueMirrorId.KEY, super.getValueMirrorId());
			oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
		}
		catch (Exception e) {}
		
		super.getParentTerm().appendPacket(oView);
		
		super.show();
	}
}
