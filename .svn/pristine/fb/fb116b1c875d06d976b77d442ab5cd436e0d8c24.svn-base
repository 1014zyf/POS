package virtualui;

import java.util.HashMap;
import org.json.JSONObject;

public class VirtualUIHorizontalList extends VirtualUIBasicElement {
	private HashMap<Integer, Object> m_oOptionMap;
	
	// Constructor
	public VirtualUIHorizontalList(){
		super.setUIType(HeroActionProtocol.View.Type.HORIZONTAL_LIST);
		super.setEnabled(true);
	}
	
	public HashMap<Integer, Object> getList(){
		return m_oOptionMap;
	}
	
	public void setList(HashMap<Integer, Object> mList){
		m_oOptionMap = mList;
	}
	
	public void addToList(Object oObject){
		m_oOptionMap.put(m_oOptionMap.size()+1 , oObject);
	}
	
	@Override
	public void show(){
		// Create packet to update UI
		JSONObject oView = new JSONObject();
		JSONObject oAttribute = new JSONObject();
		VirtualUIBasicElement parentElement = this.getParent();
		
		// Check if parent is shown before
		if(!parentElement.isShow())
			return;
		
		try {
			oAttribute.put(HeroActionProtocol.View.Attribute.Width.KEY, super.getWidth());
			oAttribute.put(HeroActionProtocol.View.Attribute.Height.KEY, super.getHeight());
			oAttribute.put(HeroActionProtocol.View.Attribute.Top.KEY, super.getTop());
			oAttribute.put(HeroActionProtocol.View.Attribute.Left.KEY, super.getLeft());
			oAttribute.put(HeroActionProtocol.View.Attribute.Background.KEY, super.getBackgroundColor());
			oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, super.getVisible());
			oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, super.getEnabled());
			oView.put(HeroActionProtocol.View.Id.KEY, super.getIDForPosting());
			oView.put(HeroActionProtocol.View.Type.KEY, super.getUIType());
			oView.put(HeroActionProtocol.View.ParentId.KEY, parentElement.getIDForPosting());
			oView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
		}
		catch (Exception e) {}
		
		super.getParentTerm().appendPacket(oView);
		
		super.show();
	}
}
