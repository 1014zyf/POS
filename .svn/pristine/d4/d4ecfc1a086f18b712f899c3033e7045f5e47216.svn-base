package virtualui;

import org.json.JSONObject;

public class VirtualUIWebView extends VirtualUIBasicElement {
	private String m_sSource;
	
	// Constructor
	public VirtualUIWebView(){
		super.setUIType(HeroActionProtocol.View.Type.WEB_VIEW);
	}
	
	public String getSource(){
		return m_sSource;
	}
	
	public void setSource(String sValue){
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
	
	@Override
	public void show(){
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oAttribute = new JSONObject();
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
			oAttribute.put(HeroActionProtocol.View.Attribute.Source.KEY, m_sSource);
			oAttribute.put(HeroActionProtocol.View.Attribute.Background.KEY, super.getBackgroundColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, super.getVisible());
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
			
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
