package virtualui;

import org.json.JSONArray;
import org.json.JSONObject;

public class VirtualUIForwarder extends VirtualUIBasicElement {
	
	// Constructor
	public VirtualUIForwarder(){
		super.setUIType(HeroActionProtocol.View.Type.FORWARDER);
	}
	
	@Override
	public void show(){
		// Check if the current element is shown before or not
		if(this.isShow())
			// Show before, return
			return;
		
		super.show();
		
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oAttribute = new JSONObject();
		JSONObject oForwardEvent = new JSONObject();
		JSONArray oForwardEventArray = new JSONArray();
		JSONObject oEvent = new JSONObject();
		
		try {
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
			
			if(super.isAllowForward()){
				JSONObject oServerRequest = new JSONObject();
				JSONObject oForwardRequest = new JSONObject();
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.Id.KEY, "Forward1");
				oServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.Note.KEY, super.getForwardForwardRequestVersion()+"");
				if(super.getForwardServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.SubmitId.KEY, super.getForwardServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.Forward.ServerRequest.BlockUI.KEY, super.getForwardServerRequestBlockUI());
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ServerRequest.KEY, oServerRequest);
				oForwardRequest.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Type.KEY, super.getForwardForwardRequestType());
				oForwardRequest.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Address.KEY, super.getForwardForwardRequestAddress());
				oForwardRequest.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Port.KEY, super.getForwardForwardRequestPort());
				oForwardRequest.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Value.KEY, super.getForwardForwardRequestValue());
				oForwardRequest.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Timeout.KEY, super.getForwardForwardRequestTimeout());
				oForwardRequest.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.Delay.KEY, super.getForwardForwardRequestDelay());
				oForwardEvent.put(HeroActionProtocol.View.Event.Forward.ForwardRequest.KEY, oForwardRequest);
				oForwardEventArray.put(oForwardEvent);
				oEvent.put(HeroActionProtocol.View.Event.Forward.KEY, oForwardEventArray);
			}
			
			oView.put(HeroActionProtocol.View.Id.KEY, super.getIDForPosting());
			oView.put(HeroActionProtocol.View.Type.KEY, super.getUIType());
			oView.put(HeroActionProtocol.View.ValueMirrorId.KEY, super.getValueMirrorId());
			oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		super.getParentTerm().appendPacket(oView);
		
		super.show();
	}
}
