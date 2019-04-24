package virtualui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VirtualUIAction {
	
	private boolean m_bHideKeyboard;
	private ArrayList<VirtualUIBasicElement> m_sReplaceValueElements;
	private ArrayList<String> m_sReplaceValueRegexs;
	private ArrayList<String> m_sReplaceValues;
	private String m_sEnableElementId;
	private boolean m_bEnableElement;
	private ArrayList<VirtualUIBasicElement> m_oVisibleElements;
	
	// Constructor
	public VirtualUIAction(){
		m_bHideKeyboard = false;
		m_sReplaceValueElements = new ArrayList<VirtualUIBasicElement>();
		m_sReplaceValueRegexs = new ArrayList<String>();
		m_sReplaceValues = new ArrayList<String>();
		m_sEnableElementId = "";
		m_bEnableElement = true;
		m_oVisibleElements = new ArrayList<VirtualUIBasicElement>();
	}
	
	public void setHideKeyboard(boolean bAction){
		m_bHideKeyboard = bAction;
	}
	
	public boolean getHideKeyboard(){
		return m_bHideKeyboard;
	}
	
	public void setReplaceValue(VirtualUIBasicElement oElement, String sReplaceValueRegex, String sReplaceValue){
		m_sReplaceValueElements.add(oElement);
		m_sReplaceValueRegexs.add(sReplaceValueRegex);
		m_sReplaceValues.add(sReplaceValue);
	}
	
	public void addVisibleElement(VirtualUIBasicElement oElement){
		m_oVisibleElements.add(oElement);
	}
	
	public JSONArray buildActionViewArray(){
		JSONArray oViewArray = new JSONArray();
		
		// Replace value
		int i = 0;
		for(i=0; i<m_sReplaceValueRegexs.size(); i++){
			JSONObject oActionView = new JSONObject();
			JSONObject oDo = new JSONObject();
			JSONObject oReplaceValue = new JSONObject();
			
			try {
				if (m_sReplaceValueElements.get(i) == null)
					oActionView.put(HeroActionProtocol.View.Id.KEY, HeroActionProtocol.View.Id.FOCUS);
				else
					oActionView.put(HeroActionProtocol.View.Id.KEY, m_sReplaceValueElements.get(i).getIDForPosting());
				oReplaceValue.put(HeroActionProtocol.View.Do.ReplaceValue.Regex.KEY, m_sReplaceValueRegexs.get(i));
				oReplaceValue.put(HeroActionProtocol.View.Do.ReplaceValue.Value.KEY, m_sReplaceValues.get(i));
				oDo.put(HeroActionProtocol.View.Do.ReplaceValue.KEY, oReplaceValue);
				oActionView.put(HeroActionProtocol.View.Do.KEY, oDo);
				oViewArray.put(oActionView);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		// Enable
		if(m_sEnableElementId.length() > 0){
			JSONObject oActionView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				oActionView.put(HeroActionProtocol.View.Id.KEY, m_sEnableElementId);
				oAttribute.put(HeroActionProtocol.View.Attribute.Enabled.KEY, m_bEnableElement);
				oActionView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
				
				oViewArray.put(oActionView);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		for(i=0; i<m_oVisibleElements.size(); i++){
			JSONObject oActionView = new JSONObject();
			JSONObject oAttribute = new JSONObject();
			
			try {
				m_oVisibleElements.get(i).setId(m_oVisibleElements.get(i).getReferenceElement().getId());
				m_oVisibleElements.get(i).setParentForm(m_oVisibleElements.get(i).getReferenceElement().getParent().getParentForm());
				
				oActionView.put(HeroActionProtocol.View.Id.KEY, m_oVisibleElements.get(i).getIDForPosting());
				oAttribute.put(HeroActionProtocol.View.Attribute.Visible.KEY, m_oVisibleElements.get(i).getVisible());
				oActionView.put(HeroActionProtocol.View.Attribute.KEY, oAttribute);
				oViewArray.put(oActionView);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return oViewArray;
	}
	
	public int getReplaceValueCount(){
		return m_sReplaceValueRegexs.size();
	}
	
	public void setClickElementEnable(String sElementId, boolean bEnable){
		m_sEnableElementId = sElementId;
		m_bEnableElement = bEnable;
	}

}
