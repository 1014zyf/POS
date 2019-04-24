package virtualui;

import org.json.JSONArray;
import org.json.JSONObject;

public class VirtualUIScrollFrame extends VirtualUIBasicElement {
	private int m_iContentWidth;
	private int m_iContentHeight;
	
	// Constructor
	public VirtualUIScrollFrame(){
		super.setUIType(HeroActionProtocol.View.Type.SCROLL_FRAME);
	}
	
	public int getContentWidth(){
		return m_iContentWidth;
	}
	
	public void setContentWidth(int iContentWidth){
		m_iContentWidth = iContentWidth;
		
		if(super.isShow()){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.ContentWidth.KEY, m_iContentWidth);
				
				oView.put(HeroActionProtocol.View.Id.KEY, super.getIDForPosting());
				oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
			}
			catch (Exception e) {}
			
			super.getParentTerm().appendPacket(oView);
		}
	}
	
	public int getContentHeight(){
		return m_iContentHeight;
	}
	
	public void setContentHeight(int iContentHeight){
		m_iContentHeight = iContentHeight;
		
		if(super.isShow()){
			// The UI is shown ==> Modify UI
			// Create packet to update UI
			JSONObject oView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oAttribute.put(HeroActionProtocol.View.Attribute.ContentHeight.KEY, m_iContentHeight);
				
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
			oAttribute.put(HeroActionProtocol.View.Attribute.ContentWidth.KEY, m_iContentWidth);
			oAttribute.put(HeroActionProtocol.View.Attribute.ContentHeight.KEY, m_iContentHeight);
			oAttribute.put(HeroActionProtocol.View.Attribute.Background.KEY, super.getBackgroundColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, super.getVisible());
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
			
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
			oView.put(HeroActionProtocol.View.Event.KEY, oEvent);
		}
		catch (Exception e) {}
		
		super.getParentTerm().appendPacket(oView);
		
		super.show();
	}
}
