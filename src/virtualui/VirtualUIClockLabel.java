package virtualui;

import org.json.JSONArray;
import org.json.JSONObject;

public class VirtualUIClockLabel extends VirtualUIBasicElement {
	private boolean m_bDisable;
	
	// Constructor
	public VirtualUIClockLabel(){
		super.setUIType(HeroActionProtocol.View.Type.CLOCK_LABEL);
		m_bDisable = false;
	}
	
	public void setDisable(boolean bDisable){
		m_bDisable = bDisable;
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
		JSONObject oSwipeTopEvent = new JSONObject();
		JSONArray oSwipeTopEventArray = new JSONArray();
		JSONObject oSwipeBottomEvent = new JSONObject();
		JSONArray oSwipeBottomEventArray = new JSONArray();
		JSONObject oSwipeRightEvent = new JSONObject();
		JSONArray oSwipeRightEventArray = new JSONArray();
		JSONObject oSwipeLeftEvent = new JSONObject();
		JSONArray oSwipeLeftEventArray = new JSONArray();
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
			oAttribute.put(HeroActionProtocol.View.Attribute.Value.KEY, super.getValue());
			oAttribute.put(HeroActionProtocol.View.Attribute.TextAlign.KEY, super.getTextAlign());
			oAttribute.put(HeroActionProtocol.View.Attribute.TextStyle.KEY, super.getTextStyle());
			oAttribute.put(HeroActionProtocol.View.Attribute.Padding.KEY, super.getPaddingValue());
			if (super.getTextSize() != 0)
				oAttribute.put(HeroActionProtocol.View.Attribute.TextSize.KEY, super.getTextSize());
			oAttribute.put(HeroActionProtocol.View.Attribute.Background.KEY, super.getBackgroundColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Foreground.KEY, super.getForegroundColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Stroke.KEY, super.getStroke());
			oAttribute.put(HeroActionProtocol.View.Attribute.StrokeColor.KEY, super.getStrokeColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Gradient.KEY, super.isGradient());
			oAttribute.put(HeroActionProtocol.View.Attribute.CornerRadius.KEY, super.getCornerRadius());
			oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, super.getVisible());
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
			oAttribute.put(HeroActionProtocol.View.Attribute.ViewSeq.KEY, super.getViewSeq()-1);
			
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
			
			if(super.isAllowSwipeTop()){
				JSONObject oServerRequest = new JSONObject();
				oSwipeTopEvent.put(HeroActionProtocol.View.Event.SwipeTop.Id.KEY, "SwipeTop1");
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.Note.KEY, super.getSwipeTopServerRequestNote());
				if(super.getSwipeTopServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.SubmitId.KEY, super.getSwipeTopServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.BlockUI.KEY, super.getSwipeTopServerRequestBlockUI());
				oSwipeTopEvent.put(HeroActionProtocol.View.Event.SwipeTop.ServerRequest.KEY, oServerRequest);
				oSwipeTopEventArray.put(oSwipeTopEvent);
				oEvent.put(HeroActionProtocol.View.Event.SwipeTop.KEY, oSwipeTopEventArray);
			}
			
			if(super.isAllowSwipeBottom()){
				JSONObject oServerRequest = new JSONObject();
				oSwipeBottomEvent.put(HeroActionProtocol.View.Event.SwipeBottom.Id.KEY, "SwipeBottom1");
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.Note.KEY, super.getSwipeBottomServerRequestNote());
				if(super.getSwipeBottomServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.SubmitId.KEY, super.getSwipeBottomServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.BlockUI.KEY, super.getSwipeBottomServerRequestBlockUI());
				oSwipeBottomEvent.put(HeroActionProtocol.View.Event.SwipeBottom.ServerRequest.KEY, oServerRequest);
				oSwipeBottomEventArray.put(oSwipeBottomEvent);
				oEvent.put(HeroActionProtocol.View.Event.SwipeBottom.KEY, oSwipeBottomEventArray);
			}
			
			if(super.isAllowSwipeRight()){
				JSONObject oServerRequest = new JSONObject();
				oSwipeRightEvent.put(HeroActionProtocol.View.Event.SwipeRight.Id.KEY, "SwipeRight1");
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.Note.KEY, super.getSwipeRightServerRequestNote());
				if(super.getSwipeRightServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.SubmitId.KEY, super.getSwipeRightServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.BlockUI.KEY, super.getSwipeRightServerRequestBlockUI());
				oSwipeRightEvent.put(HeroActionProtocol.View.Event.SwipeRight.ServerRequest.KEY, oServerRequest);
				oSwipeRightEventArray.put(oSwipeRightEvent);
				oEvent.put(HeroActionProtocol.View.Event.SwipeRight.KEY, oSwipeRightEventArray);
			}
			
			if(super.isAllowSwipeLeft()){
				JSONObject oServerRequest = new JSONObject();
				oSwipeLeftEvent.put(HeroActionProtocol.View.Event.SwipeLeft.Id.KEY, "SwipeLeft1");
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.Note.KEY, super.getSwipeLeftServerRequestNote());
				if(super.getSwipeLeftServerRequestSubmitId().length() > 0)
					oServerRequest.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.SubmitId.KEY, super.getSwipeLeftServerRequestSubmitId());
				oServerRequest.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.BlockUI.KEY, super.getSwipeLeftServerRequestBlockUI());
				oSwipeLeftEvent.put(HeroActionProtocol.View.Event.SwipeLeft.ServerRequest.KEY, oServerRequest);
				oSwipeLeftEventArray.put(oSwipeLeftEvent);
				oEvent.put(HeroActionProtocol.View.Event.SwipeLeft.KEY, oSwipeLeftEventArray);
			}
			
			oView.put(HeroActionProtocol.View.Id.KEY, super.getIDForPosting());
			oView.put(HeroActionProtocol.View.Type.KEY, super.getUIType());
			oView.put(HeroActionProtocol.View.ParentId.KEY, parentElement.getIDForPosting());
			oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			
			if (!m_bDisable)
				oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
		}
		catch (Exception e) {}
		
		super.getParentTerm().appendPacket(oView);
		
		super.show();
	}
}
